package com.snapbizz.core.sync.downloadSyncDto

import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.entities.ProductCustomization
import com.snapbizz.core.utils.DownSyncConfig
import java.util.Date

data class ProductCustomizationDto(
    @SerializedName("updated_at") val updatedAt: Date? = null,

    @SerializedName("is_offer") val isOffer: Boolean = false,

    @SerializedName("is_visibility") val isVisibility: Boolean = false,

    @SerializedName("created_at") val createdAt: Date? = null,

    @SerializedName("is_snaporder") val isSnapOrder: Boolean = false,

    @SerializedName("is_quick_add") val isQuickAdd: Boolean = false,

    @SerializedName("product_code") val productCode: Long = 0,

    @SerializedName("is_snaporder_deal") val isSnapOrderDeal: Boolean = false
)

fun apiProductCustomizationsToEntity(productCustomizationDto: ProductCustomizationDto): ProductCustomization {
    return ProductCustomization(
        productCode = productCustomizationDto.productCode,
        isQuickAdd = productCustomizationDto.isQuickAdd,
        isOffer = productCustomizationDto.isOffer,
        isVisibility = productCustomizationDto.isVisibility,
        isSnapOrder = productCustomizationDto.isSnapOrder,
        createdAt = productCustomizationDto.createdAt,
        updatedAt = productCustomizationDto.updatedAt,
        isVisibilitySync = false,
        isSnapOrderDeal = productCustomizationDto.isSnapOrderDeal,
        isSyncPending = false
    )
}

fun productCustomizationToApi(productCustomization: ProductCustomization): ProductCustomizationDto {
    return ProductCustomizationDto(
        updatedAt = productCustomization.updatedAt,
        isOffer = productCustomization.isOffer,
        isVisibility = productCustomization.isVisibility,
        createdAt = productCustomization.createdAt,
        isSnapOrder = productCustomization.isSnapOrder,
        isQuickAdd = productCustomization.isQuickAdd,
        productCode = productCustomization.productCode,
        isSnapOrderDeal = productCustomization.isSnapOrderDeal
    )
}

fun productCustomizationsToApiList(productCustomizations: List<ProductCustomization>): List<ProductCustomizationDto> {
    return productCustomizations.map { productCustomization ->
        productCustomizationToApi(productCustomization)
    }
}

fun getProductCustomizationSyncConfig(snapDb: SnapDatabase): DownSyncConfig<ProductCustomization, ProductCustomizationDto> {
    return DownSyncConfig(
        tableName = "product_customizations",
        jsonKey = "product_customizationsList",
        entityClass = ProductCustomization::class.java,
        dtoClass = ProductCustomizationDto::class.java,
        daoProvider = { snapDb.productCustomizationDao() },
        dtoToEntityMapper = ::apiProductCustomizationsToEntity
    )
}

//suspend fun upSyncProductCustomization(
//    dao: ProductCustomizationDao, syncApiService: SyncApiService, onStart: (String) -> Unit
//) {
//    onStart("Syncing Product Packs")
//    try {
//        syncData(
//            dao = dao,
//            primaryKeyColumn = "PRODUCT_CODE",
//            syncStatusColumn = "IS_SYNC_PENDING",
//            tableName = "PRODUCT_CUSTOMIZATION",
//            apiUrl = "v3/api/${Preferences.STORE_ID}/product_customizations",
//            convertToApiObjectList = ::productCustomizationsToApiList,
//            syncApiService = syncApiService
//        )
//    } catch (e: Exception) {
//        onStart("Syncing Product Packs Failed: ${e.message}")
//    }
//}
