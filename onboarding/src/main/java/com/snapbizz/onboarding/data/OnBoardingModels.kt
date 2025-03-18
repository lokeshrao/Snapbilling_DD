package com.snapbizz.onboarding.data

import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.entities.Category

class GenerateStoreOTPAPIResponse {
    @SerializedName("status")
    var status: String? = null
}

class ApiDeviceRegistrationInput {
    @SerializedName("otp")
    var otp: Int = 0

    @SerializedName("device_id")
    var deviceId: String? = null

    @SerializedName("store_phone")
    var storePhone: Long = 0
}
class ExistingStoreResponse {
    @SerializedName("status")
    var status: String? = null
    @SerializedName("store_types")
    var storeTypes: List<String>? = null
}

class ApiGenerateOTPInputDetails {
    @SerializedName("os_type")
    var osType: String = "Android"

    @SerializedName("os_version")
    var osVersion: String? = null

    @SerializedName("build_nos")
    var buildNos: String? = null

    @SerializedName("model_no")
    var modelNo: String? = null

    @SerializedName("device_id")
    var deviceId: String? = null

    @SerializedName("store_phone")
    var storePhone: Long = 0

    @SerializedName("device_type")
    var deviceType: String? = null
}

data class CategoryInfo (
    @SerializedName("id"           ) var id         : Int?     = null,
    @SerializedName("name"         ) var name       : String?  = null,
    @SerializedName("parent_id"    ) var parentId   : Int?     = null,
    @SerializedName("is_quick_add" ) var isQuickAdd : Boolean? = null,
)

data class CategoriesData (

    @SerializedName("category"     ) var category    : ArrayList<Category> = arrayListOf(),
    @SerializedName("sub_category" ) var subCategory : ArrayList<Category> = arrayListOf()

)
data class CategoriesDataResponse (

    @SerializedName("status" ) var status : String? = null,
    @SerializedName("item"   ) var item   : CategoriesData?  = CategoriesData()

){
    fun getAllCategoriesJoined(): List<Category> {
        val categories = item?.category ?: emptyList()
        val subCategories = item?.subCategory ?: emptyList()
        return categories + subCategories.map {
            Category(it.id, it.name, it.parentId, imageUrl = it.imageUrl)
        }
    }
}
data class RequestPaymentLogs(

    @SerializedName("store_id") var storeId: Long? = null,
    @SerializedName("device_id") var deviceId: String? = null,
    @SerializedName("transaction_id") var transactionId: String? = null,
    @SerializedName("user_name") var userName: String? = null,
    @SerializedName("app_key") var appKey: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("logs") var logs: String? = null,
    @SerializedName("apk_info") var apkInfo: String? = null

)
