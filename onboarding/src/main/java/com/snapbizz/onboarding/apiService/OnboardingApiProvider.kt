package com.snapbizz.onboarding.apiService

import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.core.network.NetworkModule
import com.snapbizz.core.utils.ApiURL
import com.snapbizz.core.utils.Preferences
import com.snapbizz.onboarding.downSync.GenerateTokenService
import com.snapbizz.onboarding.downSync.SyncApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
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

    @Singleton
    @Provides
    fun provideSyncApiService(
        snapDataStore: SnapDataStore,
        generateTokenService: GenerateTokenService,
    ): SyncApiService {
        val client = provideHttpClient(
            snapDataStore,
            generateTokenService
        )
        return Retrofit.Builder().baseUrl(ApiURL.BASE_URL_V3).client(client).build().create(SyncApiService::class.java)
    }

    private fun provideHttpClient(
        snapDataStore: SnapDataStore,
        generateTokenService: GenerateTokenService,
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val originalRequest = chain.request()
            Preferences.SYNC_TOKEN = snapDataStore.getSyncToken().toString()
            val newRequest = originalRequest.newBuilder()
                .header("content_type", "application/json;charset=UTF-8")
                .header("authorization", Preferences.SYNC_TOKEN)
                .header("store_id", Preferences.STORE_ID.toString())
                .header("device_id", Preferences.DEVICE_ID)
                .header("access_token", Preferences.ACCESS_TOKEN).build()

            try {
                var response = chain.proceed(newRequest)
                if (response.code == 401) {
                    response.close()

                    val newToken = generateTokenService.genJWTToken(
                        ApiGenerateJWTTokenInput(
                            Preferences.STORE_ID, Preferences.DEVICE_ID, Preferences.ACCESS_TOKEN
                        )
                    )?.execute()?.body()?.token

                    if (newToken != null) {
                        Preferences.SYNC_TOKEN = newToken
                        snapDataStore.setSyncToken(newToken)

                        val newRequestWithToken = originalRequest.newBuilder()
                            .header("content_type", "application/json;charset=UTF-8")
                            .header("authorization", Preferences.SYNC_TOKEN)
                            .header("store_id", Preferences.STORE_ID.toString())
                            .header("device_id", Preferences.DEVICE_ID)
                            .header("access_token", Preferences.ACCESS_TOKEN).build()

                        response = chain.proceed(newRequestWithToken)
                    }
                }

                return@addInterceptor response

            } catch (e: ProtocolException) {
                if (e.message?.contains("Too many follow-up requests") == true) {
                    val newToken = generateTokenService.genJWTToken(
                        ApiGenerateJWTTokenInput(
                            Preferences.STORE_ID, Preferences.DEVICE_ID, Preferences.ACCESS_TOKEN
                        )
                    )?.execute()?.body()?.token

                    if (newToken != null) {
                        Preferences.SYNC_TOKEN = newToken
                        hphSharedPreferences.setSyncToken(newToken)

                        val newRequestWithToken = originalRequest.newBuilder()
                            .header("content_type", "application/json;charset=UTF-8")
                            .header("authorization", Preferences.SYNC_TOKEN)
                            .header("store_id", Preferences.STORE_ID.toString())
                            .header("device_id", Preferences.DEVICE_ID)
                            .header("access_token", Preferences.ACCESS_TOKEN).build()

                        return@addInterceptor chain.proceed(newRequestWithToken)
                    }
                }
                throw e
            }
        }.build()
    }
}