package com.snapbizz.core.network
import com.snapbizz.common.models.ApiGenerateJWTTokenInput
import com.snapbizz.common.models.ApiGenerateJWTTokenResponse
import com.snapbizz.common.models.AppKeysResponse
import com.snapbizz.common.models.AuthorisedTokenResponse
import com.snapbizz.common.models.GenerateTokenRequestBody
import com.snapbizz.common.models.SyncApiService
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.core.utils.ApiURL
import com.snapbizz.core.utils.Dispatchers.provideDispatcher
import com.snapbizz.core.utils.SnapPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.net.ProtocolException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Volatile
    private var retrofitInstance: Retrofit? = null
    private var currentBaseUrl: String? = null

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Lazy<Retrofit?> {
        return lazy { getRetrofitInstance(ApiURL.BASE_URL,okHttpClient) }
    }

    private fun getRetrofitInstance(baseUrl: String,okHttpClient: OkHttpClient, forceCreate: Boolean = false): Retrofit {
        return if (forceCreate || retrofitInstance == null || currentBaseUrl != baseUrl) {
            synchronized(this) {
                if (forceCreate || retrofitInstance == null || currentBaseUrl != baseUrl) {
                    retrofitInstance = createRetrofitInstance(baseUrl,okHttpClient)
                    currentBaseUrl = baseUrl
                }
                retrofitInstance!!
            }
        } else {
            retrofitInstance!!
        }
    }

    fun createRetrofitInstance(baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    @Singleton
    @Provides
    fun provideTokenService(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): GenerateTokenService {
        return Retrofit.Builder().baseUrl(ApiURL.BASE_URL_V3).client(okHttpClient)
            .addConverterFactory(converterFactory).build().create(GenerateTokenService::class.java)
    }
    @Singleton
    @Provides
    fun provideTokenV4Service(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): GenerateTokenV4Service {
        return Retrofit.Builder().baseUrl(ApiURL.BASE_URL_V4).client(okHttpClient)
            .addConverterFactory(converterFactory).build().create(GenerateTokenV4Service::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
        okHttpClient.callTimeout(40, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(40, TimeUnit.SECONDS)
        okHttpClient.readTimeout(40, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(40, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(loggingInterceptor)
        okHttpClient.build()
        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }
    @Singleton
    @Provides
    fun provideLoggingProvider(loggingInterceptor: HttpLoggingInterceptor): LoggingProvider {
        return object : LoggingProvider {
            override fun getLoggingInterceptor(): HttpLoggingInterceptor {
                return loggingInterceptor
            }
        }
    }

    @Provides
    fun provideSyncApiService(
        snapDataStore: SnapDataStore,
        generateTokenService: GenerateTokenService,
        loggingProvider:LoggingProvider
    ): SyncApiService {
        val client = provideHttpClient(
            snapDataStore,
            generateTokenService,
            loggingProvider
        )
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()) // ✅ Required for JsonObject
            .baseUrl(ApiURL.BASE_URL_V3).client(client).build().create(SyncApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideHomeApiService(
        snapDataStore: SnapDataStore,
        generateTokenV4Service: GenerateTokenV4Service,
        loggingProvider:LoggingProvider
    ): HomeApiService {
        val client = provideTokenV4HttpClient(
            snapDataStore,
            generateTokenV4Service,
            loggingProvider
        )
        return Retrofit.Builder().baseUrl(ApiURL.BASE_URL_V4).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build().create(HomeApiService::class.java)
    }
}
private fun provideHttpClient(
    snapDataStore: SnapDataStore,
    generateTokenService: GenerateTokenService,
    loggingProvider: LoggingProvider,
): OkHttpClient {

    val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        CoroutineScope(provideDispatcher().io).launch {
            SnapPreferences.SYNC_TOKEN = snapDataStore.getSyncToken()?.first().toString()
        }
        val newRequest = originalRequest.newBuilder()
            .header("content_type", "application/json;charset=UTF-8")
            .header("authorization", SnapPreferences.SYNC_TOKEN)
            .header("store_id", SnapPreferences.STORE_ID.toString())
            .header("device_id", SnapPreferences.DEVICE_ID)
            .header("access_token", SnapPreferences.ACCESS_TOKEN)
            .build()

        try {
            var response = chain.proceed(newRequest)

            if (response.code == 401) {  // Handle unauthorized access
                response.close()

                val newToken = generateTokenService.genJWTToken(
                    ApiGenerateJWTTokenInput(
                        SnapPreferences.STORE_ID, SnapPreferences.DEVICE_ID, SnapPreferences.ACCESS_TOKEN
                    )
                )?.execute()?.body()?.token

                if (newToken != null) {
                    SnapPreferences.SYNC_TOKEN = newToken
                    CoroutineScope(provideDispatcher().io).launch {
                        snapDataStore.setSyncToken(newToken)
                    }

                    val newRequestWithToken = originalRequest.newBuilder()
                        .header("content_type", "application/json;charset=UTF-8")
                        .header("authorization", SnapPreferences.SYNC_TOKEN)
                        .header("store_id", SnapPreferences.STORE_ID.toString())
                        .header("device_id", SnapPreferences.DEVICE_ID)
                        .header("access_token", SnapPreferences.ACCESS_TOKEN)
                        .build()

                    response = chain.proceed(newRequestWithToken)
                }
            }

            return@Interceptor response

        } catch (e: ProtocolException) {
            if (e.message?.contains("Too many follow-up requests") == true) {
                val newToken = generateTokenService.genJWTToken(
                    ApiGenerateJWTTokenInput(
                        SnapPreferences.STORE_ID, SnapPreferences.DEVICE_ID, SnapPreferences.ACCESS_TOKEN
                    )
                )?.execute()?.body()?.token

                if (newToken != null) {
                    SnapPreferences.SYNC_TOKEN = newToken
                    CoroutineScope(provideDispatcher().io).launch {
                        snapDataStore.setSyncToken(newToken)
                    }

                    val newRequestWithToken = originalRequest.newBuilder()
                        .header("content_type", "application/json;charset=UTF-8")
                        .header("authorization", SnapPreferences.SYNC_TOKEN)
                        .header("store_id", SnapPreferences.STORE_ID.toString())
                        .header("device_id", SnapPreferences.DEVICE_ID)
                        .header("access_token", SnapPreferences.ACCESS_TOKEN)
                        .build()

                    return@Interceptor chain.proceed(newRequestWithToken)
                }
            }
            throw e
        }
    }

    // ✅ Build OkHttpClient with Interceptors
    return OkHttpClient.Builder()
        .addInterceptor(loggingProvider.getLoggingInterceptor()) // Logging for debugging
        .addInterceptor(authInterceptor)    // Token management
        .protocols(listOf(Protocol.HTTP_1_1)) // Avoids protocol issues
        .build()
}

private fun provideTokenV4HttpClient(
    snapDataStore: SnapDataStore,
    generateTokenV4Service: GenerateTokenV4Service,
    loggingProvider: LoggingProvider,
): OkHttpClient {
    val authInterceptor = Interceptor { chain ->
        var token = runBlocking { snapDataStore.getAuthTokenV4() }

        if (token.isNullOrBlank()) {
            runBlocking {
                fetchAndStoreV4Token(snapDataStore, generateTokenV4Service)
            }
            token = SnapPreferences.v4Token
        }

        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        var response = chain.proceed(newRequest)

        if (response.code == 401) {
            response.close()
            runBlocking {
                fetchAndStoreV4Token(snapDataStore, generateTokenV4Service)
            }
            val retryRequest = chain.request().newBuilder()
                .addHeader("Authorization", "${SnapPreferences.v4Token}")
                .build()

            response = chain.proceed(retryRequest)
        }

        response
    }

    return OkHttpClient.Builder()
        .addInterceptor(loggingProvider.getLoggingInterceptor())
        .addInterceptor(authInterceptor)
        .protocols(listOf(Protocol.HTTP_1_1))
        .build()
}


suspend fun fetchAndStoreV4Token(
    snapDataStore: SnapDataStore,
    generateTokenV4Service: GenerateTokenV4Service
) {
    try {
        val request = GenerateTokenRequestBody().apply {
            this.accessToken = SnapPreferences.ACCESS_TOKEN
            this.deviceId = SnapPreferences.DEVICE_ID
            this.storeId = SnapPreferences.STORE_ID
        }

        val response = generateTokenV4Service.getTokenV4(request).execute()
        val result = response.body()

        if (response.isSuccessful && result != null && !result.token.isNullOrEmpty()) {
            SnapPreferences.v4Token = result.token!!
            snapDataStore.setAuthTokenV4(result.token?:"")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        // Handle/log error if needed
    }
}

interface GenerateTokenService {
    @POST("v3/api/generate_token")
    fun genJWTToken(@Body apiGenerateJWTTokenInput: ApiGenerateJWTTokenInput?): Call<ApiGenerateJWTTokenResponse?>?
}
interface LoggingProvider {
    fun getLoggingInterceptor(): HttpLoggingInterceptor
}
interface GenerateTokenV4Service {
    @POST("v1/api/auth/token")
    fun getTokenV4(@Body body: GenerateTokenRequestBody?): Call<AuthorisedTokenResponse>
}

interface HomeApiService {
    @GET("v1/api/store/tid_details")
    fun getAppKeys(
        @Header("store_id") storeId: String,
        @Header("device_id") deviceId: String,
        @Header("access_token") accessToken: String
    ): Call<AppKeysResponse>

}