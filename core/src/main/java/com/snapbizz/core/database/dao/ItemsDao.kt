package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.snapbizz.core.database.entities.Items

@Dao
interface ItemsDao : GenericDao<Items> {

    @Query("SELECT * FROM ITEMS WHERE _id = :id")
    suspend fun getItemById(id: Long): Items?

    @Query("SELECT * FROM ITEMS WHERE INVOICE_ID = :invoiceId")
    suspend fun getItemsByInvoiceId(invoiceId: Long): List<Items>

    @Query("SELECT * FROM ITEMS WHERE invoice_id IN (:invoiceIds)")
    suspend fun getItemsByInvoiceIds(invoiceIds: List<Long>): List<Items>

    @Query("SELECT * FROM ITEMS WHERE PRODUCT_CODE = :productCode")
    suspend fun getItemsByProductCode(productCode: Long): List<Items>

    @Query("DELETE FROM ITEMS WHERE _id = :id")
    suspend fun deleteItemById(id: Long)

    @Query("UPDATE ITEMS SET NAME = :name WHERE _id = :id")
    suspend fun updateItemName(id: Long, name: String?)

    @Query("DELETE FROM ITEMS")
    fun deleteAll()
}
