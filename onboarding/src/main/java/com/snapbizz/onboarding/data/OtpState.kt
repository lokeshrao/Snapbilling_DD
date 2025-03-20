package com.snapbizz.onboarding.data

data class OtpState(
    val isOtpSent: Boolean=false,
    val isVerified: Boolean=false,
    val deviceId: String="",
    val phoneNo: String="",
    val otp: String="",
    val isLoading:Boolean=false,
    val message :String? = null
)
