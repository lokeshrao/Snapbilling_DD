package com.snapbizz.core.sync.downloadSyncDto

import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.entities.ProductPacks
import com.snapbizz.core.utils.DownSyncConfig
import java.util.Date

data class ProductPacksDto(
    @SerializedName("product_code")
    var productCode: Long = 0,

    @SerializedName("pack_size")
    var packSize: Int? = null,

    @SerializedName("sale_price1")
    var salePrice1: Long? = null,

    @SerializedName("sale_price2")
    var salePrice2: Long? = null,

    @SerializedName("sale_price3")
    var salePrice3: Long? = null,

    @SerializedName("purchase_price")
    var purchasePrice: Long? = null,

    @SerializedName("is_default")
    var isDefault: Boolean = false,

    @SerializedName("is_deleted")
    var isDeleted: Boolean = false,

    @SerializedName("created_at")
    var createdAt: Date? = null,

    @SerializedName("updated_at")
    var updatedAt: Date? = null,

    @SerializedName("barcode")
    var barcode: Long = 0,

    @SerializedName("mrp")
    var mrp: Long = 0,

    @SerializedName("exp_date")
    var expDate: Date? = null
)

fun productPacksDtoToEntity(apiProductPacks: ProductPacksDto): ProductPacks {
    return ProductPacks(
        id = null,
        productCode = apiProductPacks.productCode,
        packSize = apiProductPacks.packSize ?: 0,
        salePrice1 = apiProductPacks.salePrice1 ?: 0,
        salePrice2 = apiProductPacks.salePrice2 ?: 0,
        salePrice3 = apiProductPacks.salePrice3 ?: 0,
        purchasePrice = apiProductPacks.purchasePrice ?: 0,
        isDefault = apiProductPacks.isDefault,
        isDeleted = apiProductPacks.isDeleted,
        createdAt = apiProductPacks.createdAt,
        updatedAt = apiProductPacks.updatedAt,
        barcode = apiProductPacks.barcode,
        mrp = apiProductPacks.mrp,
        expDate = apiProductPacks.expDate,
        isSyncPending = false,
        isSnapOrderSync = false
    )
}

fun getProductPackSyncConfig(snapDb: SnapDatabase): DownSyncConfig<ProductPacks, ProductPacksDto> {
    return DownSyncConfig(
        tableName = "product_packs",
        jsonKey = "product_packsList",
        entityClass = ProductPacks::class.java,
        dtoClass = ProductPacksDto::class.java,
        daoProvider = { snapDb.productPacksDao() },
        dtoToEntityMapper = ::productPacksDtoToEntity
    )
}

fun productPacksToDto(productPacks: ProductPacks): ProductPacksDto {
    return ProductPacksDto(
        productCode = productPacks.productCode,
        packSize = productPacks.packSize,
        salePrice1 = productPacks.salePrice1,
        salePrice2 = productPacks.salePrice2,
        salePrice3 = productPacks.salePrice3,
        purchasePrice = productPacks.purchasePrice,
        isDefault = productPacks.isDefault,
        isDeleted = productPacks.isDeleted,
        createdAt = productPacks.createdAt,
        updatedAt = productPacks.updatedAt,
        barcode = productPacks.barcode?:0,
        mrp = productPacks.mrp?:0,
        expDate = productPacks.expDate
    )
}
fun productPacksListToDto(productPacksList: List<ProductPacks>): List<ProductPacksDto> {
    return productPacksList.map { productPacks ->
        productPacksToDto(productPacks)
    }
}

//suspend fun upSyncProductPacks(
//    dao: ProductPacksDao,
//    syncApiService: SyncApiService,
//    onStart: (String) -> Unit
//) {
//    onStart("Syncing Product Packs")
//    try {
//        syncData(
//            dao = dao,
//            primaryKeyColumn = "_id",
//            syncStatusColumn = "IS_SYNC_PENDING",
//            tableName = "PRODUCT_PACKS",
//            apiUrl = "v3/api/${Preferences.STORE_ID}/product_packs",
//            convertToApiObjectList = ::productPacksListToDto,
//            syncApiService = syncApiService
//        )
//    } catch (e: Exception) {
//        onStart("Syncing Product Packs Failed: ${e.message}")
//    }
//}
