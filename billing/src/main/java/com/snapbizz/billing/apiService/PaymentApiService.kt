package com.snapbizz.billing.apiService

import com.snapbizz.common.models.AppKeysResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface PaymentApiService {
    @GET("v1/api/store/tid_details")
    fun getAppKeys(
        @Header("store_id") storeId: String,
        @Header("device_id") deviceId: String,
        @Header("access_token") accessToken: String,
        @Header("Authorization") authToken: String,
    ): Call<AppKeysResponse>
}