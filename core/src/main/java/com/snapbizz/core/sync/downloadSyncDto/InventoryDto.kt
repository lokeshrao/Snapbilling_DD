package com.snapbizz.core.sync.downloadSyncDto

import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.entities.Inventory
import com.snapbizz.core.utils.DownSyncConfig
import java.util.Date

data class InventoryDto(
    @SerializedName("minimum_base_quantity")
    var minimumBaseQuantity: Long? = null,

    @SerializedName("reorder_point")
    var reorderPoint: Long? = null,

    @SerializedName("quantity")
    var quantity: Int = 0,

    @SerializedName("product_code")
    var productCode: Long = 0,

    @SerializedName("is_deleted")
    var isDeleted: Boolean = false,

    @SerializedName("created_at")
    var createdAt: Date? = null,

    @SerializedName("updated_at")
    var updatedAt: Date? = null
)

fun inventoryDtoToEntity(apiInventory: InventoryDto): Inventory {
    return Inventory(
        productCode = apiInventory.productCode,
        quantity = apiInventory.quantity,
        minimumBaseQuantity = apiInventory.minimumBaseQuantity,
        reOrderPoint = apiInventory.reorderPoint,
        isDeleted = apiInventory.isDeleted,
        createdAt = apiInventory.createdAt,
        updatedAt = apiInventory.updatedAt,
        isSyncPending = false
    )
}

fun getInventorySyncConfig(snapDb: SnapDatabase): DownSyncConfig<Inventory, InventoryDto> {
    return DownSyncConfig(
        tableName = "inventory",
        jsonKey = "inventoriesList",
        entityClass = Inventory::class.java,
        dtoClass = InventoryDto::class.java,
        daoProvider = { snapDb.inventoryDao() },
        dtoToEntityMapper = ::inventoryDtoToEntity
    )
}

fun inventoryToUpSyncDto(inventory: Inventory): InventoryDto {
    return InventoryDto(
        minimumBaseQuantity = inventory.minimumBaseQuantity?:0,
        reorderPoint = inventory.reOrderPoint?:0,
        quantity = inventory.quantity,
        productCode = inventory.productCode,
        isDeleted = inventory.isDeleted,
        createdAt = inventory.createdAt,
        updatedAt = inventory.updatedAt
    )
}
fun inventoryListToUpSyncDtoList(inventories: List<Inventory>): List<InventoryDto> {
    return inventories.map { inventory -> inventoryToUpSyncDto(inventory) }
}
//suspend fun upSyncInventories(
//    dao: InventoryDao,
//    syncApiService: SyncApiService,
//    onStart: (String) -> Unit
//) {
//    onStart("Syncing Inventories")
//    syncData(
//        dao,
//        primaryKeyColumn = "PRODUCT_CODE",
//        syncStatusColumn = "IS_SYNC_PENDING",
//        tableName = "INVENTORY",
//        apiUrl = "v3/api/${Preferences.STORE_ID}/inventory",
//        convertToApiObjectList = ::inventoryListToUpSyncDtoList,
//        syncApiService = syncApiService
//    )
//}
