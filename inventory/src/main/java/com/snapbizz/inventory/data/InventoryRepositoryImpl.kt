package com.snapbizz.inventory.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.snapbizz.common.config.InventoryRepository
import com.snapbizz.common.config.models.ProductInfo
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.SnapGlobalDatabase
import com.snapbizz.core.database.entities.ProductPacks
import com.snapbizz.core.database.entities.global.GlobalProduct
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InventoryRepositoryImpl @Inject constructor(
    private val snapDatabase: SnapDatabase,
    val snapGlobalDatabase: SnapGlobalDatabase
): InventoryInternalRepo, InventoryRepository {
    override fun getGlobalProducts(query: String): Flow<PagingData<GlobalProduct>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                snapGlobalDatabase.globalProductDao().getProducts(query)
            }).flow
    }

    override fun addNewProducts(value: ProductDetailsInfo?): Boolean {
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

    override fun getAllProductDetails(query: String): Flow<PagingData<ProductPacks>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                snapDatabase.productPacksDao().getProductPacks()
            }).flow
    }
}