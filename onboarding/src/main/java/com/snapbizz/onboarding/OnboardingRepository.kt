package com.snapbizz.onboarding

import android.os.Build
import com.snapbizz.core.utils.ResourceProvider
import com.snapbizz.core.utils.SnapConstants.DEVICE_TYPE
import com.snapbizz.core.utils.SnapConstants.STORE_TYPE
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val apiService: OnboardingApiService?,
    private val resourceProvider: ResourceProvider
)  {

    suspend fun sendOtp(phoneNo: Long, deviceId: String): Result<Unit> {
        return try {
            val existingStoreResponse = apiService?.checkExistingStore(phoneNo, deviceId)?.execute()
            val storeResult = existingStoreResponse?.body()

            if (existingStoreResponse?.isSuccessful != true) {
                return Result.failure(Exception(resourceProvider.getString(R.string.otp_send_failure)))
            }
            when {
                storeResult?.status.equals("Found") -> {
                    if (storeResult?.storeTypes?.contains(STORE_TYPE) == true) {
                        generateOtp(phoneNo, deviceId)
                    } else {
                        Result.failure(Exception(resourceProvider.getString(R.string.registration_error)))
                    }
                }

                storeResult?.status.equals("Not Found") -> {
                    Result.failure(Exception(resourceProvider.getString(R.string.store_not_found_info)))
                }

                else -> {
                    Result.failure(Exception(resourceProvider.getString(R.string.otp_send_failure)))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception(resourceProvider.getString(R.string.otp_send_failure)))
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

    suspend fun verifyOtp(
        phoneNo: Long, otp: Int, deviceId: String
    ): Result<StoreDetailsResponse> {
        return try {
            ApiDeviceRegistrationInput().apply {
                this.deviceId = deviceId
                this.storePhone = phoneNo
                this.otp = otp
            }.let {
                val response = apiService?.verify(it)?.execute()
                val result = response?.body()
                if (response?.isSuccessful == true && result?.status?.equals("Success") == true) {
                    if (result.storeType?.contains(STORE_TYPE) == true) {
                        Result.success(result)
                    } else {
                        Result.failure(Exception(resourceProvider.getString(R.string.registration_error)))
                    }
                } else {
                    Result.failure(
                        Exception(
                            result?.status
                                ?: resourceProvider.getString(R.string.otp_verification_failure)
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception(resourceProvider.getString(R.string.otp_verification_failure)))
        }
    }
}

