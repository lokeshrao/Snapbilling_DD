package com.snapbizz.onboarding.apiService

import com.snapbizz.common.models.StoreDetailsResponse
import com.snapbizz.onboarding.data.ApiDeviceRegistrationInput
import com.snapbizz.onboarding.data.ApiGenerateOTPInputDetails
import com.snapbizz.onboarding.data.ExistingStoreResponse
import com.snapbizz.onboarding.data.GenerateStoreOTPAPIResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OnboardingApiService {
    @POST("otp_generation")
    fun generateOtp(@Body otpGenerationInput: ApiGenerateOTPInputDetails?): Call<GenerateStoreOTPAPIResponse?>?

    @POST("device_registration")
    fun verify(@Body verifyOtp: ApiDeviceRegistrationInput?): Call<StoreDetailsResponse?>?

    @GET("store/{phone}/{deviceId}")
    fun checkExistingStore(
        @Path("phone") phone: Long, @Path("deviceId") deviceId: String?
    ): Call<ExistingStoreResponse?>?
}