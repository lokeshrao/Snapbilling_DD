package com.snapbizz.billing.apiService

import com.snapbizz.core.utils.ApiURL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PaymentApiProvider {

//    @Singleton
//    @Provides
//    fun providePaymentApiService(retrofit: Lazy<Retrofit?>): PaymentApiService? {
//        return if (SnapThemeConfig.features.cart) retrofit.value?.create(PaymentApiService::class.java) else null
//    }

    @Singleton
    @Provides
    fun providePaymentService(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): PaymentApiService {
        return Retrofit.Builder().baseUrl(ApiURL.BASE_URL_V4).client(okHttpClient)
            .addConverterFactory(converterFactory).build().create(PaymentApiService::class.java)
    }
}