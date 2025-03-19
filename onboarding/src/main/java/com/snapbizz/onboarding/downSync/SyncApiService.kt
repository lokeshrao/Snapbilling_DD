package com.snapbizz.onboarding.downSync

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.snapbizz.common.config.models.ApiGenerateJWTTokenInput
import com.snapbizz.common.config.models.ApiGenerateJWTTokenResponse
import com.snapbizz.common.config.models.DefaultAPIResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path



fun <U> prepareDataForUpload(gson: Gson, dataList: List<U>): JsonArray {
    val jsonArray = JsonArray()
    for (data in dataList) {
        jsonArray.add(gson.toJsonTree(data))
    }
    return jsonArray
}


