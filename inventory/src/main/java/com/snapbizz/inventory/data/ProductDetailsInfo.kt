package com.snapbizz.inventory.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.snapbizz.core.database.entities.Inventory
import com.snapbizz.core.database.entities.ProductCustomization
import com.snapbizz.core.database.entities.ProductPacks
import com.snapbizz.core.database.entities.Products
import java.util.Date

data class ProductDetailsInfo(
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

fun convertToProduct(product: ProductDetailsInfo?): Products? {
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

fun convertToProductPacks(product: ProductDetailsInfo?): ProductPacks? {
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

fun convertToProductCustomization(cpProduct: ProductDetailsInfo?): ProductCustomization? {
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

fun convertToInventory(product: ProductDetailsInfo?): Inventory? {
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
