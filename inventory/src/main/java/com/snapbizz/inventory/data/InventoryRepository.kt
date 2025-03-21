package com.snapbizz.inventory.data

import com.snapbizz.core.database.dao.InventoryDao
import com.snapbizz.core.database.entities.Inventory
import java.util.Date
import javax.inject.Inject

class InventoryRepository @Inject constructor(
    private val inventoryDao: InventoryDao
) {

    fun addNewProducts(value: Inventory): Long {
        return inventoryDao.insert(value)
    }
}