package com.snapbizz.horticulture.sync.downSync

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.snapbizz.onboarding.downSync.ApiGenerateJWTTokenInput
import com.snapbizz.onboarding.downSync.ApiGenerateJWTTokenResponse
import com.snapbizz.onboarding.downSync.DefaultAPIResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface SyncApiService {
    @GET("v3/api/{storeId}/{table}")
    suspend fun getData(
        @Path("table") table: String,
        @Path("storeId") storeId: Long,
        @Header("offset") offset: String
    ): Response<JsonObject>?

    @POST("{table}")
    suspend fun uploadData(
        @Path("table" , encoded = true) table: String, @Body data: JsonArray
    ): DefaultAPIResponse?

//    @PUT("{storeId}/{table}")
//    suspend fun uploadUpdatedData(
//        @Path("table") table: String, @Path("storeId") storeId: Long, @Body data: JsonArray
//    ): DefaultAPIResponse?

}

interface GenerateTokenService {
    @POST("v3/api/generate_token")
    fun genJWTToken(@Body apiGenerateJWTTokenInput: ApiGenerateJWTTokenInput?): Call<ApiGenerateJWTTokenResponse?>?
}

fun <U> prepareDataForUpload(gson: Gson, dataList: List<U>): JsonArray {
    val jsonArray = JsonArray()
    for (data in dataList) {
        jsonArray.add(gson.toJsonTree(data))
    }
    return jsonArray
}


