package com.snapbizz.common.models

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

data class StoreDetailsResponse(
    @SerializedName("store_details") val storeDetails: StoreDetails? = null,

    @SerializedName("access_token") val accessToken: String? = null,

    @SerializedName("retailer_details") val retailerDetails: RetailerDetails? = null,

    @SerializedName("store_type") val storeType: List<String>? = null,

    @SerializedName("status") val status: String? = null,
    var posId: Int?=null
)

data class RetailerDetails(
    @SerializedName("retailer_id") val retailerId: Long = 0,

    @SerializedName("name") val name: String? = null,

    @SerializedName("address") val address: String? = null,

    @SerializedName("email") val email: String? = null,

    @SerializedName("gstin") val gstin: String? = null
)

data class StoreDetails(
    @SerializedName("store_id") val storeId: Long = 0,

    @SerializedName("is_disabled") val isDisabled: Boolean = false,

    @SerializedName("salesperson_number") val salespersonNumber: Long? = null,

    @SerializedName("city") val city: String? = null,

    @SerializedName("state") val state: String? = null,

    @SerializedName("tin") val tin: Long? = null,

    @SerializedName("pincode") val pincode: Int = 0,

    @SerializedName("phone") val phone: Long = 0,

    @SerializedName("name") val name: String? = null,

    @SerializedName("address") val address: String? = null,

    @SerializedName("retailer_id") val retailerId: Long = 0,

    @SerializedName("state_id") val stateId: Int = 0,

    @SerializedName("retailer_gstin") val retailerGstin: String? = null
)

data class ApiGenerateJWTTokenInput(
    @SerializedName("store_id") var storeId: Long = 0,
    @SerializedName("device_id") var deviceId: String? = null,
    @SerializedName("access_token") var accessToken: String? = null
)

data class ApiGenerateJWTTokenResponse(
    @SerializedName("status") var status: String? = null,
    @SerializedName("token") var token: String? = null
)
data class DefaultAPIResponse (
    @SerializedName("status") var status: String? = null
)

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
