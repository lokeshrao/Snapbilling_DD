package com.snapbizz.core.network
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Volatile
    private var retrofitInstance: Retrofit? = null
    private var currentBaseUrl: String? = null

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return getRetrofitInstance("https://jsonplaceholder.typicode.com/")
    }

//    @Provides
//    @Singleton
//    fun provideApiService(retrofit: Retrofit): ApiService {
//        return retrofit.create(ApiService::class.java)
//    }

    private fun getRetrofitInstance(baseUrl: String, forceCreate: Boolean = false): Retrofit {
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

    private fun createRetrofitInstance(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
