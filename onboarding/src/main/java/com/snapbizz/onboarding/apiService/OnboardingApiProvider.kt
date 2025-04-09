package com.snapbizz.onboarding.apiService

import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.core.network.RetrofitBaseV2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingApiProvider {

    @Singleton
    @Provides
    fun provideOnboardingApiService(@RetrofitBaseV2 retrofit: Retrofit?): OnboardingApiService? {
        return if (SnapThemeConfig.features.cart) retrofit?.create(OnboardingApiService::class.java) else null
    }
}