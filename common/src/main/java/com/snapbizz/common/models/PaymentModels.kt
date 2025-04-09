package com.snapbizz.common.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PaymentModels {
}

data class PaymentData(
    val amount: Double,
    val transactionId: String,
    val customerName: String?,
    val phoneNumber: String?,
    val remark: String?,
    val type: Type? = null,
    val paymentSource: PaymentSource? = null,
    val additionalParams: Map<String, String> = emptyMap()
)

sealed class PaymentResult {
    data class Success(val txnId: String, val message: String) : PaymentResult()
    data class Failure(val errorCode: String, val errorMessage: String) : PaymentResult()
    object Loading : PaymentResult()
}

data class AppKeysResponse(

    @SerializedName("data") var appKeysData: AppKeysData? = AppKeysData(),
    @SerializedName("subCode") var subCode: Int? = null,

): ApiResponse()

data class AppKeysData(

    @SerializedName("tid") var tid: String? = null,
    @SerializedName("mid") var mid: String? = null,
    @SerializedName("store_id") var storeId: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("app_key") var appKey: String? = null,
    @SerializedName("user_name") var userName: String? = null,
    @SerializedName("merchant_name") var merchantName: String? = null

)

data class AuthorisedTokenResponse (
    @SerializedName("token") var token: String? = null
): ApiResponse()

class GenerateTokenRequestBody {
    @SerializedName("access_token") var accessToken: String? = null

    @SerializedName("device_id") var deviceId: String? = null

    @SerializedName("store_id") var storeId: Long = 0
}

open class ApiResponse(
    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: String? = null
)

enum class PaymentSource() {
    CUSTOMERS,
    QUICK_PAY,
    BILLING
}
enum class Type() {
    PAYMENT,
    CHECK_STATUS,
    AUTO_CHECK_STATUS,
    VOID,
    REPRINT
}
