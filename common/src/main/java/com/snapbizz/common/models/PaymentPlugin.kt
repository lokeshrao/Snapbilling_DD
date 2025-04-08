package com.snapbizz.common.models

import android.app.Application
import android.content.Context
import android.content.Intent

interface PaymentPlugin {

    fun initSdk(context: Context): Any?

    fun getPaymentRequestIntent(context: Context, paymentData: PaymentData): Intent?

    fun getCheckStatusIntent(transactionId: String): Intent?

    fun validatePaymentResponse(data: Intent?): PaymentResult

}


