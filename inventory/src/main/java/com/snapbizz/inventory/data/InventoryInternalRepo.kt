package com.snapbizz.inventory.data

import androidx.paging.PagingData
import com.snapbizz.common.models.ProductInfo
import com.snapbizz.core.database.entities.global.GlobalProduct
import kotlinx.coroutines.flow.Flow

interface InventoryInternalRepo {
    fun getGlobalProducts(query: String): Flow<PagingData<GlobalProduct>>
    fun addNewProducts(value: ProductDetailsInfo?): Boolean
    fun getAllProductDetails(query: String?): Flow<PagingData<ProductInfo>>
}