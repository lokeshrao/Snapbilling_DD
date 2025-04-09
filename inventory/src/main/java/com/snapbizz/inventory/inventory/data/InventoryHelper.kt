package com.snapbizz.inventory.inventory.data

import com.snapbizz.common.models.ProductInfo
import com.snapbizz.core.database.entities.Inventory
import com.snapbizz.core.database.entities.ProductCustomization
import com.snapbizz.core.database.entities.ProductPacks
import com.snapbizz.core.database.entities.Products
import java.util.Date


fun convertToProduct(product: ProductInfo?): Products? {
    return try {
        Products(
            product?.productCode?.toLongOrNull()?:0L,
            product?.name.toString(),
            product?.mrp?.toLong()?:0L,
            product?.uom.toString(),
            0.0,
            0,
            product?.image.toString(),
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
            product?.productCode?.toLongOrNull()?:0L,
            false,
            isSyncPending = true,
            isExclusiveGst = false
        )
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}

fun convertToProductPacks(product: ProductInfo?): ProductPacks? {
    return try {
        ProductPacks(null,
            product?.productCode?.toLongOrNull()?:0L,
            1,
            product?.mrp?.toLong() ?: 0L,
            product?.mrp?.toLong() ?: 0L,
            product?.mrp?.toLong() ?: 0L,
            product?.mrp?.toLong() ?: 0L,
            isDefault = true,
            isDeleted = false,
            createdAt = Date(),
            updatedAt = Date(),
            barcode = product?.productCode?.toLongOrNull()?:0L,
            mrp = product?.mrp?.toLong() ?: 0L,
            isSnapOrderSync = false,
            expDate = Date(),
            isSyncPending = true
        )
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}

fun convertToProductCustomization(cpProduct: ProductInfo?): ProductCustomization? {
    return try {
        ProductCustomization(
            cpProduct?.productCode?.toLongOrNull()?:0L,
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

fun convertToInventory(product: ProductInfo?): Inventory? {
    return try {
        Inventory(
            product?.productCode?.toLongOrNull()?:0L,
            product?.inventoryQuantity?:0,
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
