package com.snapbizz.core.network
import android.util.Log
import com.snapbizz.common.config.models.ApiGenerateJWTTokenInput
import com.snapbizz.common.config.models.ApiGenerateJWTTokenResponse
import com.snapbizz.common.config.models.SyncApiService
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.core.utils.ApiURL
import com.snapbizz.core.utils.Dispatchers.provideDispatcher
import com.snapbizz.core.utils.SnapPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
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

interface GenerateTokenService {
    @POST("v3/api/generate_token")
    fun genJWTToken(@Body apiGenerateJWTTokenInput: ApiGenerateJWTTokenInput?): Call<ApiGenerateJWTTokenResponse?>?
}
interface LoggingProvider {
    fun getLoggingInterceptor(): HttpLoggingInterceptor
}