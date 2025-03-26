package com.snapbizz.onboarding.data

import android.os.Build
import com.snapbizz.common.config.SnapResult
import com.snapbizz.common.models.StoreDetailsResponse
import com.snapbizz.core.utils.ResourceProvider
import com.snapbizz.core.utils.SnapConstants.DEVICE_TYPE
import com.snapbizz.core.utils.SnapConstants.STORE_TYPE
import com.snapbizz.onboarding.R
import com.snapbizz.onboarding.apiService.OnboardingApiService
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val apiService: OnboardingApiService?,
    private val resourceProvider: ResourceProvider
)  {

    suspend fun sendOtp(phoneNo: Long, deviceId: String): SnapResult<Unit> {
        return try {
            val existingStoreResponse = apiService?.checkExistingStore(phoneNo, deviceId)?.execute()
            val storeResult = existingStoreResponse?.body()

            if (existingStoreResponse?.isSuccessful != true) {
                return SnapResult.Error(resourceProvider.getString(R.string.otp_send_failure))
            }

            when (storeResult?.status) {
                "Found" -> {
                    if (storeResult.storeTypes?.contains(STORE_TYPE) == true) {
                        generateOtp(phoneNo, deviceId)
                        SnapResult.Success(Unit)
                    } else {
                        SnapResult.Error(resourceProvider.getString(R.string.registration_error))
                    }
                }
                "Not Found" -> {
                    SnapResult.Error(resourceProvider.getString(R.string.store_not_found_info))
                }
                else -> {
                    SnapResult.Error(resourceProvider.getString(R.string.otp_send_failure))
                }
            }
        } catch (e: Exception) {
            SnapResult.Error(resourceProvider.getString(R.string.otp_send_failure))
        }
    }
    private suspend fun generateOtp(phoneNo: Long, deviceId: String): Result<Unit> {
        val otpInputDetails = ApiGenerateOTPInputDetails().apply {
            this.deviceId = deviceId
            this.storePhone = phoneNo
            this.deviceType = DEVICE_TYPE
            this.osVersion = Build.VERSION.SDK_INT.toString()
            this.buildNos = "" //TD
            this.modelNo = Build.MODEL
        }

        val response = apiService?.generateOtp(otpInputDetails)?.execute()
        val result = response?.body()

        return when {
            response?.isSuccessful == true && result?.status.equals("Success") -> {
                Result.success(Unit)
            }

            result?.status.equals("Device attached to another store") -> {
                Result.failure(Exception(resourceProvider.getString(R.string.device_attached_error)))
            }

            else -> {
                Result.failure(
                    Exception(
                        result?.status ?: resourceProvider.getString(R.string.otp_send_failure)
                    )
                )
            }
        }
    }

    suspend fun verifyOtp(phoneNo: Long, otp: Int, deviceId: String): SnapResult<StoreDetailsResponse> {
        return try {
            val input = ApiDeviceRegistrationInput().apply {
                this.deviceId = deviceId
                this.storePhone = phoneNo
                this.otp = otp
            }

            val response = apiService?.verify(input)?.execute()
            val result = response?.body()

            if (response?.isSuccessful == true && result?.status == "Success") {
                if (result.storeType?.contains(STORE_TYPE) == true) {
                    SnapResult.Success(result)
                } else {
                    SnapResult.Error(resourceProvider.getString(R.string.registration_error))
                }
            } else {
                SnapResult.Error(result?.status ?: resourceProvider.getString(R.string.otp_verification_failure))
            }
        } catch (e: Exception) {
            SnapResult.Error(resourceProvider.getString(R.string.otp_verification_failure))
        }
    }

//    suspend fun getCategoryData(): Result<List<Category>?> {
//        return try {
//            val response = userApiService.getProductCategories().execute()
//            val result = response.body()
//            if (response.isSuccessful &&result?.status.equals("SUCCESS",true)) {
//                val categories = result?.getAllCategoriesJoined()
//                if (!categories.isNullOrEmpty()) {
//                    categoryDao.insert(categories)
//                    Result.success(categories)
//                } else {
//                    Result.failure(Exception("No categories found"))
//                }
//            } else {
//                Result.failure(Exception("Error: ${response.errorBody()?.string() ?: "Unknown error"}"))
//            }
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//            Result.failure(Exception("Unable to get Product Categories", ex))
//        }
//    }
}

