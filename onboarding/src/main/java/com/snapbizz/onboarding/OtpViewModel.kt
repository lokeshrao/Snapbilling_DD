package com.snapbizz.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.internal.Provider
import retrofit2.Retrofit
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(private val retrofit: Lazy<Retrofit?>) : ViewModel() {


    fun getData() {
        Log.e("Hello","Fetched Retrofit ${retrofit.value}")
        var i = retrofit.value?.baseUrl()
    }

    init {
        Log.e("Hello","Init Happen")
    }
}