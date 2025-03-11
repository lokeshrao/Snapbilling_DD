package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.snapbizz.core.database.dao.GenericDao
import com.snapbizz.core.database.entities.InvoiceReference
import com.snapbizz.core.database.entities.Transactions

@Dao
interface TransactionsDao : GenericDao<Transactions> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transactions)

    @Query("SELECT * FROM TRANSACTIONS WHERE _id = :id")
    suspend fun getTransactionById(id: Long): Transactions?

    @Query("SELECT * FROM TRANSACTIONS WHERE INVOICE_ID = :id")
    suspend fun getTransactionByInvoiceId(id: Long): List<Transactions>

    @Query("SELECT * FROM TRANSACTIONS WHERE CUSTOMER_PHONE = :customerPhone")
    suspend fun getTransactionsByCustomerPhone(customerPhone: Long): List<Transactions>

    @Query("SELECT * FROM TRANSACTIONS WHERE IS_SYNC_PENDING = 0")
    suspend fun getUnsyncedTransactions(): List<Transactions>

    @Query("DELETE FROM TRANSACTIONS WHERE _id = :id")
    suspend fun deleteTransactionById(id: Long)

    @Query("UPDATE TRANSACTIONS SET IS_SYNC_PENDING = :isSync WHERE _id = :id")
    suspend fun updateSyncStatus(id: Long, isSync: Boolean)

    @Query("DELETE FROM TRANSACTIONS")
    fun deleteAll()

    @Query("SELECT IS_SYNC_PENDING FROM TRANSACTIONS WHERE _id = :id")
    suspend fun getTransactionSyncStatusById(id: Long): Int?

    @Query("SELECT MAX(_id) FROM Transactions")
    fun getLastTrxId(): Long?

    @Query(
        """
    SELECT INVOICE_ID AS invoiceId, PAYMENT_REFERENCE AS paymentReference 
    FROM Transactions 
    WHERE INVOICE_ID IN (:invoiceIds)
"""
    )
    fun getInvoiceIdToRefMap(invoiceIds: List<Long>): List<InvoiceReference>

}
