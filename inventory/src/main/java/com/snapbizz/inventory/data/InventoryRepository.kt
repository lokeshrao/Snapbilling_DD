package com.snapbizz.inventory.data

import com.snapbizz.core.database.SnapDatabase
import javax.inject.Inject

class InventoryRepository @Inject constructor(
    private val snapDatabase: SnapDatabase
) {
    fun addNewProducts(value: InventoryInfo?): Boolean {
        return snapDatabase.runInTransaction<Boolean> {
            val product = convertToProduct(value)?.let {
                snapDatabase.productsDao().insertSync(it)
            } ?: -1L
            val productPacks = convertToProductPacks(value)?.let {
                snapDatabase.productPacksDao().insertSync(it)
            } ?: -1L
            val productCustomization = convertToProductCustomization(value)?.let {
                snapDatabase.productCustomizationDao().insertSync(it)
            } ?: -1L
            val inventory = convertToInventory(value)?.let {
                snapDatabase.inventoryDao().insertSync(it)
            } ?: -1L

            return@runInTransaction product > 0 && productPacks > 0 && productCustomization > 0 && inventory > 0
        }
    }
}