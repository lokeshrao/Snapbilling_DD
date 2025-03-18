package com.snapbizz.onboarding.apiService

import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.core.network.NetworkModule
import com.snapbizz.core.utils.ApiURL
import com.snapbizz.onboarding.downSync.GenerateTokenService
import com.snapbizz.onboarding.downSync.SyncApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingApiProvider {

    @Singleton
    @Provides
    fun provideOnboardingApiService(retrofit: Lazy<Retrofit?>): OnboardingApiService? {
        return if (SnapThemeConfig.features.cart) retrofit.value?.create(OnboardingApiService::class.java) else null
    }

    @Singleton
    @Provides
    fun provideSyncApiService(
    ): SyncApiService {
        return NetworkModule.createRetrofitInstance(ApiURL.BASE_URL_V3).create(SyncApiService::class.java)
//        val client = provideHttpClient(
//            snapDatastore,
//            generateTokenService
//        )
//        return if (SnapThemeConfig.features.cart) retrofit.value?.create(SyncApiService::class.java) else null

    }
}