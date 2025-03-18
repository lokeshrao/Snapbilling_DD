package com.snapbizz.onboarding.downSync

import com.google.gson.annotations.SerializedName


data class ApiGenerateJWTTokenInput(
    @SerializedName("store_id") var storeId: Long = 0,
    @SerializedName("device_id") var deviceId: String? = null,
    @SerializedName("access_token") var accessToken: String? = null
)

data class ApiGenerateJWTTokenResponse(
    @SerializedName("status") var status: String? = null,
    @SerializedName("token") var token: String? = null
)
data class DefaultAPIResponse (
    @SerializedName("status") var status: String? = null
)