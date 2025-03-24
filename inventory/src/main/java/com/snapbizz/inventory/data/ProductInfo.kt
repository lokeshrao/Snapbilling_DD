package com.snapbizz.inventory.data
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.snapbizz.core.database.entities.Inventory
import com.snapbizz.core.database.entities.ProductCustomization
import com.snapbizz.core.database.entities.ProductPacks
import com.snapbizz.core.database.entities.Products
import java.util.Date

data class ProductInfo(
    var productCode: Long?,
    var name: String?,
    var mrp: Long?,
    var uom: String?,
    val vatRate: Double?,
    val categoryId: Int?,
    var categoryName: String?,
    var parentCategoryName: String?,
    val image: String?,
    val isGdb: Boolean?,
    val isDeleted: Boolean?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val hsnCode: String?,
    val sgstRate: Double?,
    val cgstRate: Double?,
    val igstRate: Double?,
    val cessRate: Double?,
    val additionalCessRate: Double?,
    val barcode: Long?,
    val isSnapOrderSync: Boolean?,
    val isSyncPending: Boolean?,
    val isExclusiveGst: Boolean?,
    val packSize: Int?,
    val salePrice1: Long?,
    val salePrice2: Long?,
    val salePrice3: Long?,
    var purchasePrice: Long?,
    val isDefault: Boolean?,
    val packBarcode: Long?,
    val expDate: Date?,
    var inventoryQuantity: Long?,
    val minimumBaseQuantity: Long?,
    val reOrderPoint: Long?,
    val isQuickAdd: Boolean?,
    val farmerShare: Long?
)


data class InventoryInfo(
    var productCode: MutableState<String?> = mutableStateOf(""),
    var productName: MutableState<String?> = mutableStateOf(""),
    var productMrp: MutableState<String?> = mutableStateOf(""),
    var productUom: MutableState<String?> = mutableStateOf(""),
    var productPP: MutableState<String?> = mutableStateOf(""),
    var productImage: MutableState<String?> = mutableStateOf(""),
    var quantity: MutableState<String?> = mutableStateOf(""),
    var minimumBaseQuantity: MutableState<String?> = mutableStateOf(""),
    var reOrderPoint: MutableState<String> = mutableStateOf(""),
    var isDeleted: MutableState<String> = mutableStateOf(""),
    var createdAt: MutableState<String> = mutableStateOf(""),
    var updatedAt: MutableState<String> = mutableStateOf(""),
    var isSyncPending: MutableState<String> = mutableStateOf("")
)

fun convertToProduct(product: InventoryInfo?): Products? {
    return try {
        Products(
            product?.productCode?.value?.toLongOrNull()?:0L,
            product?.productName?.value.toString(),
            product?.productMrp?.value?.toLongOrNull()?:0L,
            product?.productUom?.value.toString(),
            0.0,
            0,
            product?.productImage?.value.toString(),
            false,
            false,
            Date(),
            Date(),
            "",
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            product?.productCode?.value?.toLongOrNull()?:0L,
            false,
            isSyncPending = true,
            isExclusiveGst = false
        )

    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}

fun convertToProductPacks(product: InventoryInfo?): ProductPacks? {
    return try {
        ProductPacks(null,
            product?.productCode?.value?.toLongOrNull()?:0L,
            1,
            product?.productMrp?.value?.toLongOrNull() ?: 0L,
            product?.productMrp?.value?.toLongOrNull() ?: 0L,
            product?.productMrp?.value?.toLongOrNull() ?: 0L,
            product?.productMrp?.value?.toLongOrNull() ?: 0L,
            isDefault = true,
            isDeleted = false,
            createdAt = Date(),
            updatedAt = Date(),
            barcode = product?.productCode?.value?.toLongOrNull()?:0L,
            mrp = product?.productMrp?.value?.toLongOrNull() ?: 0L,
            isSnapOrderSync = false,
            expDate = Date(),
            isSyncPending = true
        )
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}

fun convertToProductCustomization(cpProduct: InventoryInfo?): ProductCustomization? {
    return try {
        ProductCustomization(
            cpProduct?.productCode?.value?.toLongOrNull()?:0L,
            isQuickAdd = true,
            isOffer = false,
            isVisibility = false,
            isSnapOrder = false,
            createdAt = Date(),
            updatedAt = Date(),
            isVisibilitySync = false,
            isSnapOrderDeal = false,
            isSyncPending = true
        )
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}

fun convertToInventory(product: InventoryInfo?): Inventory? {
    return try {
        Inventory(
            product?.productCode?.value?.toLongOrNull()?:0L,
            product?.quantity?.value?.toIntOrNull()?:0,
            0L,
            0L,
            false,
            Date(),
            Date(),
            true
        )
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}