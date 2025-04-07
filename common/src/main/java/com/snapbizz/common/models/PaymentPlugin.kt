package com.snapbizz.common.models

import android.app.Application
import android.content.Intent

interface PaymentPlugin {

    fun initSdk(application: Application)

    fun getPaymentRequestIntent(paymentData: PaymentData): Intent?

    fun getCheckStatusIntent(transactionId: String): Intent?

    fun validatePaymentResponse(data: Intent?): PaymentResult

}


