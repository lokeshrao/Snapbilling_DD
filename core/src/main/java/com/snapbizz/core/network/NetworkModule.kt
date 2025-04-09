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
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @RetrofitBaseV2
    @Provides
    @Singleton
    fun provideRetrofitV2(okHttpClient: OkHttpClient): Retrofit? {
        return createRetrofitInstance(ApiURL.BASE_URL, okHttpClient)
    }

    @RetrofitBaseV3
    @Provides
    @Singleton
    fun provideRetrofitV3(@HttpClientV3Client okHttpClient: OkHttpClient): Retrofit? {
        return createRetrofitInstance(ApiURL.BASE_URL_V3, okHttpClient)
    }

    @RetrofitBaseV4
    @Provides
    @Singleton
    fun provideRetrofitV4(@HttpClientV4Client okHttpClient: OkHttpClient): Retrofit? {
        return createRetrofitInstance(ApiURL.BASE_URL_V4, okHttpClient)
    }

    fun createRetrofitInstance(baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideTokenService(
        okHttpClient: OkHttpClient, converterFactory: Converter.Factory
    ): GenerateTokenService {
        return Retrofit.Builder().baseUrl(ApiURL.BASE_URL_V3).client(okHttpClient)
            .addConverterFactory(converterFactory).build().create(GenerateTokenService::class.java)
    }

    @Singleton
    @Provides
    fun provideTokenV4Service(
        okHttpClient: OkHttpClient, converterFactory: Converter.Factory
    ): GenerateTokenV4Service {
        return Retrofit.Builder().baseUrl(ApiURL.BASE_URL_V4).client(okHttpClient)
            .addConverterFactory(converterFactory).build()
            .create(GenerateTokenV4Service::class.java)
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

    @Singleton
    @Provides
    fun provideSyncApiService(
        @RetrofitBaseV3 retrofit: Retrofit?,
    ): SyncApiService? {
        return retrofit?.create(SyncApiService::class.java)
    }

    @Provides
    fun provideHomeApiService(
        @RetrofitBaseV4 retrofit: Retrofit?,
    ): HomeApiService? {
        return retrofit?.create(HomeApiService::class.java)
    }

    @Provides
    @Singleton
    @HttpClientV3Client
    fun provideSyncHttpClient(
        snapDataStore: SnapDataStore,
        generateTokenService: GenerateTokenService,
        loggingProvider: LoggingProvider,
    ): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()

            var token = runBlocking { snapDataStore.getSyncToken() ?: "" }

            var request = originalRequest.newBuilder()
                .header("content_type", "application/json;charset=UTF-8")
                .header("authorization", token)
                .header("store_id", SnapPreferences.STORE_ID.toString())
                .header("device_id", SnapPreferences.DEVICE_ID)
                .header("access_token", SnapPreferences.ACCESS_TOKEN).build()

            var response = chain.proceed(request)
            val resCode = response.code
            if (resCode in 400..499){
                response.close()

                val newToken = generateTokenService.genJWTToken(
                    ApiGenerateJWTTokenInput(
                        SnapPreferences.STORE_ID,
                        SnapPreferences.DEVICE_ID,
                        SnapPreferences.ACCESS_TOKEN
                    )
                )?.execute()?.body()?.token

                if (!newToken.isNullOrBlank()) {
                    SnapPreferences.SYNC_TOKEN = newToken
                    runBlocking { snapDataStore.setSyncToken(newToken) }

                    request = originalRequest.newBuilder()
                        .header("content_type", "application/json;charset=UTF-8")
                        .header("authorization", newToken)
                        .header("store_id", SnapPreferences.STORE_ID.toString())
                        .header("device_id", SnapPreferences.DEVICE_ID)
                        .header("access_token", SnapPreferences.ACCESS_TOKEN).build()

                    response = chain.proceed(request)
                }
            }

            response
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor).addInterceptor(loggingProvider.getLoggingInterceptor()).protocols(listOf(Protocol.HTTP_1_1)).build()
    }

    @Provides
    @Singleton
    @HttpClientV4Client
    fun provideTokenV4HttpClient(
        snapDataStore: SnapDataStore,
        generateTokenV4Service: GenerateTokenV4Service,
        loggingProvider: LoggingProvider,
    ): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            var token = runBlocking { snapDataStore.getAuthTokenV4() }

            if (token.isNullOrBlank()) {
                runBlocking { fetchAndStoreV4Token(snapDataStore, generateTokenV4Service) }
                token = SnapPreferences.v4Token
            }

            var request =
                chain.request().newBuilder().addHeader("Authorization", "$token").build()

            var response = chain.proceed(request)
            val resCode = response.code
            if (resCode in 400..499){
                response.close()

                runBlocking { fetchAndStoreV4Token(snapDataStore, generateTokenV4Service) }

                token = SnapPreferences.v4Token
                request =
                    chain.request().newBuilder().addHeader("Authorization", "$token").build()

                response = chain.proceed(request)
            }

            response
        }

        return OkHttpClient.Builder().addInterceptor(loggingProvider.getLoggingInterceptor())
            .addInterceptor(authInterceptor).protocols(listOf(Protocol.HTTP_1_1)).build()
    }
}


suspend fun fetchAndStoreV4Token(
    snapDataStore: SnapDataStore, generateTokenV4Service: GenerateTokenV4Service
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
            snapDataStore.setAuthTokenV4(result.token ?: "")
        }
    } catch (e: Exception) {
        e.printStackTrace()
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
    suspend fun getAppKeys(
        @Header("store_id") storeId: String,
        @Header("device_id") deviceId: String,
        @Header("access_token") accessToken: String
    ): AppKeysResponse?

}

