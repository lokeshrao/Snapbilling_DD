package com.snapbizz.axis.home

import com.snapbizz.core.utils.ApiURL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton


//@Module
//@InstallIn(SingletonComponent::class)
//object ApiProvider {
//    @Singleton
//    @Provides
//    fun providePaymentService(
//        okHttpClient: OkHttpClient,
//        converterFactory: Converter.Factory
//    ): HomeApiService {
//        return Retrofit.Builder().baseUrl(ApiURL.BASE_URL_V4).client(provideTokenV4HttpClient)
//            .addConverterFactory(converterFactory).build().create(HomeApiService::class.java)
//    }
//}