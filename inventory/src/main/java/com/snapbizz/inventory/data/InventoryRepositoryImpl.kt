package com.snapbizz.inventory.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.snapbizz.common.config.InventoryRepository
import com.snapbizz.common.models.ProductInfo
import com.snapbizz.core.database.InvoiceInfo
import com.snapbizz.core.database.InvoiceWithItems
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.SnapGlobalDatabase
import com.snapbizz.core.database.entities.global.GlobalProduct
import com.snapbizz.core.helpers.LogModule
import com.snapbizz.core.helpers.SnapLogger
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

    override fun addNewProducts(value: ProductInfo?): Boolean {
        return snapDatabase.runInTransaction<Boolean> {
            val product = convertToProduct(value)?.let {
                snapDatabase.productsDao().insertOrUpdateSync(it)
            } ?: -1L
            val productPacks = convertToProductPacks(value)?.let {
                snapDatabase.productPacksDao().insertOrUpdateSync(it)
            } ?: -1L
            val productCustomization = convertToProductCustomization(value)?.let {
                snapDatabase.productCustomizationDao().insertOrUpdateSync(it)
            } ?: -1L
            val inventory = convertToInventory(value)?.let {
                snapDatabase.inventoryDao().insertOrUpdateSync(it)
            } ?: -1L

            return@runInTransaction product > 0 && productPacks > 0 && productCustomization > 0 && inventory > 0
        }
    }

    override fun getAllProductDetails(query: String?): Flow<PagingData<ProductInfo>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                snapDatabase.productPacksDao().searchProducts(query)
            }).flow
    }

    fun getInvoicesWithItems():InvoiceWithItems? {
//        var item = snapDatabase.invoiceDao().getInvoiceWithItems(17124000001)
//        SnapLogger.log("InvoiceWith Items", item.toString(), LogModule.HOME)
        return null
    }
}