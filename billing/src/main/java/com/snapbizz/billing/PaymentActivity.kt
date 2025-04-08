package com.snapbizz.billing

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.snapbizz.billing.printer.EzetapPaymentPlugin
import com.snapbizz.common.models.PaymentData
import com.snapbizz.common.models.PaymentResult
import com.snapbizz.core.utils.SnapPreferences
import com.snapbizz.ui.snapComponents.SnapButton
import com.snapbizz.ui.snapComponents.SnapProgressDialog
import com.snapbizz.ui.snapComponents.SnapSurface
import com.snapbizz.ui.snapComponents.SnapText
import org.json.JSONObject
import java.util.*

class PaymentActivity : ComponentActivity() {

    companion object {
        private const val EXTRA_PAYMENT_DATA = "extra_payment_data"
        const val EXTRA_PAYMENT_RESULT = "extra_payment_result"

        fun newIntent(context: Context, paymentData: PaymentData?): Intent {
            return Intent(context, PaymentActivity::class.java).apply {
                putExtra(EXTRA_PAYMENT_DATA, paymentData.toString())
            }
        }
    }

    private val plugin by lazy { EzetapPaymentPlugin() }
    private lateinit var paymentData: PaymentData

    private var isSdkInitialized = mutableStateOf(false)
    private var isLoading = mutableStateOf(true)

    private val sdkInitLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            isLoading.value = false

            val data = result.data
            if (result.resultCode == Activity.RESULT_OK && data != null) {
                val responseStr = data.getStringExtra("response")

                if (!responseStr.isNullOrEmpty()) {
                    try {
                        val jsonResponse = JSONObject(responseStr)
                        val resultObj = jsonResponse.optJSONObject("result")
                        val message = resultObj?.optString("message")

                        if (message.equals("Initialize device successful.", ignoreCase = true)) {
                            SnapPreferences.isSdkInit = true
                            isSdkInitialized.value = true
                            Toast.makeText(this, "Ezetap init success", Toast.LENGTH_LONG).show()

                        } else {
                            SnapPreferences.isSdkInit = false
                            isSdkInitialized.value = false
                            Toast.makeText(this, "SDK Init Failed: $message", Toast.LENGTH_LONG).show()
                            finishWithResult(PaymentResult.Failure("INIT_FAILED", message ?: "Unknown Error"))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        SnapPreferences.isSdkInit = false
                        isSdkInitialized.value = false
                        Toast.makeText(this, "Invalid SDK response", Toast.LENGTH_LONG).show()
                        finishWithResult(PaymentResult.Failure("INIT_FAILED", "Invalid response from SDK"))
                    }
                } else {
                    Toast.makeText(this, "Empty SDK response", Toast.LENGTH_LONG).show()
                    finishWithResult(PaymentResult.Failure("INIT_FAILED", "Empty response from SDK"))
                }

            } else {
                SnapPreferences.isSdkInit = false
                isSdkInitialized.value = false
                Toast.makeText(this, "SDK Init Cancelled", Toast.LENGTH_LONG).show()
                finishWithResult(PaymentResult.Failure("INIT_CANCELLED", "SDK initialization was cancelled"))
            }
        }


    private val paymentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val paymentResult = plugin.validatePaymentResponse(result.data)
            finishWithResult(paymentResult)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        paymentData = PaymentData(10.10, Date().time.toString(), "", "", "")

        if (SnapPreferences.isSdkInit) {
            isSdkInitialized.value = true
            isLoading.value = false
        } else {
            plugin.initSdk(application)?.let {
                sdkInitLauncher.launch(it)
            } ?: run {
                isLoading.value = false
                Toast.makeText(this, "Failed to launch SDK", Toast.LENGTH_LONG).show()
                finishWithResult(PaymentResult.Failure("NO_INIT_INTENT", "SDK init intent is null"))
            }
        }

        setContent {
            PaymentScreen(
                paymentData = paymentData,
                isLoading = isLoading.value,
                showPaymentScreen = isSdkInitialized.value
            ) {
                val intent = plugin.getPaymentRequestIntent(this, paymentData)
                if (intent != null) {
                    paymentLauncher.launch(intent)
                } else {
                    Toast.makeText(this, "Failed to launch payment", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun finishWithResult(result: PaymentResult) {
        val intent = Intent().apply {
            putExtra(EXTRA_PAYMENT_RESULT, result.toString())
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}


@Composable
fun PaymentScreen(
    paymentData: PaymentData, isLoading: Boolean, showPaymentScreen: Boolean, onProceed: () -> Unit
) {
    SnapSurface(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    SnapProgressDialog(isVisible = isLoading)
                }
            }

            showPaymentScreen -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    SnapText("Confirm Payment")
                    SnapText("Transaction: ${paymentData.transactionId} - ₹${paymentData.amount}")
                    Spacer(modifier = Modifier.height(16.dp))
                    SnapButton("Confirm", onProceed)
                }
            }
        }
    }
}



