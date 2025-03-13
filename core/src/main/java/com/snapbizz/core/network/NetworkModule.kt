package com.snapbizz.core.network
import android.util.Log
import com.snapbizz.core.utils.ApiURL
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
    fun provideRetrofit(): Lazy<Retrofit?> {
        return lazy { getRetrofitInstance(ApiURL.BASE_URL) }
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

    private fun createRetrofitInstance(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
