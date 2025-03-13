package com.snapbizz.onboarding

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

//interface LogService{
//    @POST("v1/api/auth/token")
//    fun sendGenerateToken(@Body body: ApiGenerateJWTTokenInput?): Call<ApiGenerateJWTTokenResponse>
//
//    @POST("v1/api/utility/axis_payment_logs")
//    fun sendPaymentLogs(
//        @Header("store_id") storeId: Long,
//        @Header("device_id") deviceId: String?,
//        @Header("access_token") accessToken: String?,
//        @Header("Authorization") auth: String?,
//        @Body requestPaymentLogs: RequestPaymentLogs
//    ): Call<ResponseBody>
//
//    @POST("v1/api/utility/app_version_log")
//    fun requestAppVersion(
//        @Header("store_id") storeId: Long,
//        @Header("Authorization") authToken: String,
//        @Header("device_id") deviceId: String,
//        @Header("access_token") accessToken: String,
//        @Body appVersionRequest: HPHModels.AppVersionRequest,
//    ): Call<HPHModels.AppVersionResponse?>?
//
//}
