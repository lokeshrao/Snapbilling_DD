package com.snapbizz.onboarding

import com.snapbizz.common.config.SnapThemeConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingApiProvider {

    @Singleton
    @Provides
    fun provideOnboardingApiService(retrofit: Lazy<Retrofit?>): OnboardingApiService? {
        return if (SnapThemeConfig.features.cart) retrofit.value?.create(OnboardingApiService::class.java) else null
    }
}