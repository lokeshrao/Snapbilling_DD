package com.snapbizz.onboarding.apiService

import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.common.config.models.ApiGenerateJWTTokenInput
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.core.network.GenerateTokenService
import com.snapbizz.core.network.LoggingProvider
import com.snapbizz.core.utils.ApiURL
import com.snapbizz.core.utils.Dispatchers.provideDispatcher
import com.snapbizz.core.utils.SnapPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import java.net.ProtocolException
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