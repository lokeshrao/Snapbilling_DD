package com.snapbizz.billing.printer
import android.app.Application
import android.content.Intent
import com.snapbizz.common.models.PaymentData
import com.snapbizz.common.models.PaymentPlugin
import com.snapbizz.common.models.PaymentResult

class EzetapPaymentPlugin : PaymentPlugin {

    override fun initSdk(application: Application) {

    }

    override fun getPaymentRequestIntent(paymentData: PaymentData): Intent? {
        return null
    }

    override fun getCheckStatusIntent(transactionId: String): Intent? {
        return null
    }

    override fun validatePaymentResponse(data: Intent?): PaymentResult {
        return if (data != null && data.getBooleanExtra("success", false)) {
            PaymentResult.Success(
                txnId = data.getStringExtra("txnId") ?: "unknown",
                message = "Payment successful"
            )
        } else {
            PaymentResult.Failure(
                errorCode = data?.getStringExtra("errorCode") ?: "UNKNOWN",
                errorMessage = data?.getStringExtra("errorMessage") ?: "Unknown error"
            )
        }
    }
}
