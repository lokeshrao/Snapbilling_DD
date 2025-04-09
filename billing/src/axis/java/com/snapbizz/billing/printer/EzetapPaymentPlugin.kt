package com.snapbizz.billing.printer

import android.content.Context
import android.content.Intent
import com.eze.api.EzeAPIActivity
import com.snapbizz.common.models.PaymentData
import com.snapbizz.common.models.PaymentPlugin
import com.snapbizz.common.models.PaymentResult
import com.snapbizz.core.utils.SnapCommonUtils
import com.snapbizz.core.utils.SnapCommonUtils.returnRequestParams
import org.json.JSONException
import org.json.JSONObject

class EzetapPaymentPlugin : PaymentPlugin {

    override fun initSdk(context: Context): Intent? {
        return try {
            val jsonRequest = JSONObject().apply {
                put("demoAppKey", "5c5a2d69-1001-498b-96c0-7f638d811591")
                put("prodAppKey", "5c5a2d69-1001-498b-96c0-7f638d811591")
                put("merchantName", "test")
                put("userName", "test")
                put("currencyCode", "INR")
                put("appMode", "Demo")
                put("captureSignature", "true")
                put("prepareDevice", "false")
            }
            Intent(context, EzeAPIActivity::class.java).apply {
                action = "initialize"
                putExtra("params", returnRequestParams(jsonRequest))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
    }

    override fun getPaymentRequestIntent(context: Context, paymentData: PaymentData): Intent? {
        return try {
            val jsonRequest = JSONObject().apply {
                put("amount", SnapCommonUtils.formatAmountToDouble(paymentData.amount))
                put("options", JSONObject().apply {
                    put("references", JSONObject().apply {
                        put("reference1", paymentData.transactionId)
                        paymentData.remark?.let { put("reference2", it) }
                    })
                })
            }
            Intent(context, EzeAPIActivity::class.java).apply {
                action = "pay"
                putExtra("params", returnRequestParams(jsonRequest))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
    }


    override fun getCheckStatusIntent(transactionId: String): Intent? {
        return null
    }

    override fun validatePaymentResponse(data: Intent?): PaymentResult {
        return if (data != null && data.getBooleanExtra("success", false)) {
            PaymentResult.Success(
                txnId = data.getStringExtra("txnId") ?: "unknown", message = "Payment successful"
            )
        } else {
            PaymentResult.Failure(
                errorCode = data?.getStringExtra("errorCode") ?: "UNKNOWN",
                errorMessage = data?.getStringExtra("errorMessage") ?: "Unknown error"
            )
        }
    }
}
