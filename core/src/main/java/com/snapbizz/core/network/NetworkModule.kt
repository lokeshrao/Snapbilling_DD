package com.snapbizz.core.network
import android.util.Log
import com.snapbizz.common.config.models.ApiGenerateJWTTokenInput
import com.snapbizz.common.config.models.ApiGenerateJWTTokenResponse
import com.snapbizz.core.utils.ApiURL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Volatile
    private var retrofitInstance: Retrofit? = null
    private var currentBaseUrl: String? = null

    @Provides
    @Singleton
    fun provideRetrofit(): Lazy<Retrofit?> {
        return lazy { getRetrofitInstance(ApiURL.BASE_URL) }
    }
    @Provides
    @Singleton
    fun provideRetrofitV3(): Lazy<Retrofit?> {
        return lazy { getRetrofitInstance(ApiURL.BASE_URL_V3) }
    }


    private fun getRetrofitInstance(baseUrl: String, forceCreate: Boolean = false): Retrofit {
        Log.d("NetworkModule", "Providing Retrofit instance")
        return if (forceCreate || retrofitInstance == null || currentBaseUrl != baseUrl) {
            synchronized(this) {
                if (forceCreate || retrofitInstance == null || currentBaseUrl != baseUrl) {
                    retrofitInstance = createRetrofitInstance(baseUrl)
                    currentBaseUrl = baseUrl
                }
                retrofitInstance!!
            }
        } else {
            retrofitInstance!!
        }
    }

    fun createRetrofitInstance(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
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
        okHttpClient: OkHttpClient, converterFactory: Converter.Factory
    ): GenerateTokenService {
        return Retrofit.Builder().baseUrl(ApiURL.BASE_URL_V3).client(okHttpClient)
            .addConverterFactory(converterFactory).build().create(GenerateTokenService::class.java)
    }
}

interface GenerateTokenService {
    @POST("v3/api/generate_token")
    fun genJWTToken(@Body apiGenerateJWTTokenInput: ApiGenerateJWTTokenInput?): Call<ApiGenerateJWTTokenResponse?>?
}