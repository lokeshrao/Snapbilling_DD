package com.snapbizz.onboarding.data

import com.snapbizz.core.utils.ResourceProvider
import com.snapbizz.onboarding.R

data class OtpState(
    val isOtpSent: Boolean=false,
    val isVerified: Boolean=false,
    val deviceId: String="",
    val phoneNo: String="",
    val otp: String="",
    val isLoading:Boolean=false,
    val message :String? = null
){
    fun validatePhone(resourceProvider: ResourceProvider): Long? {
        return phoneNo.takeIf { it.length == 10 && it.all { ch -> ch.isDigit() } }
            ?.toLongOrNull()
            ?: run {
                resourceProvider.getString(R.string.invalid_phone_number)
                null
            }
    }

    fun validatePhoneAndOtp(resourceProvider: ResourceProvider): Pair<Long, Int>? {
        val otpDigit = otp.toIntOrNull()
        return when {
            phoneNo.length != 10 || phoneNo.any { !it.isDigit() } -> {
                resourceProvider.getString(R.string.invalid_phone_number)
                null
            }

            otpDigit == null -> {
                resourceProvider.getString(R.string.otp_cannot_be_blank)
                null
            }

            else -> {
                phoneNo.toLongOrNull()?.let { phone -> Pair(phone, otpDigit) }
                    ?: run {
                        resourceProvider.getString(R.string.invalid_phone_number_format)
                        null
                    }
            }
        }
    }

}
