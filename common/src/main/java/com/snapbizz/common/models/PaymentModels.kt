package com.snapbizz.common.models

class PaymentModels {
}

data class PaymentData(
    val amount: Double,
    val transactionId: String,
    val customerName: String?,
    val phoneNumber: String?,
    val additionalParams: Map<String, String> = emptyMap()
)

sealed class PaymentResult {
    data class Success(val txnId: String, val message: String) : PaymentResult()
    data class Failure(val errorCode: String, val errorMessage: String) : PaymentResult()
    object Loading : PaymentResult()
}
