package com.snapbizz.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snapbizz.core.database.dao.Identifiable
import java.util.Date

@Entity(tableName = "TRANSACTIONS")
data class Transactions(
    @PrimaryKey @ColumnInfo(name = "_id") val id: Long = 0,
    @ColumnInfo(name = "INVOICE_ID") val invoiceId: Long?,
    @ColumnInfo(name = "PAYMENT_TYPE") val paymentType: String?,
    @ColumnInfo(name = "PAYMENT_MODE") val paymentMode: String?,
    @ColumnInfo(name = "PAYMENT_REFERENCE") val paymentReference: String?,
    @ColumnInfo(name = "AMOUNT") val amount: Long,
    @ColumnInfo(name = "MDR_VALUE") val mdrAmount: Long,
    @ColumnInfo(name = "CUSTOMER_PHONE") val customerPhone: Long?,
    @ColumnInfo(name = "PARENT_TRANSACTION_ID") val parentTransactionId: Long?,
    @ColumnInfo(name = "IS_SYNC_PENDING") var isSyncPending: Boolean,
    @ColumnInfo(name = "CREATED_AT") val createdAt: Date,
    @ColumnInfo(name = "UPDATED_AT") val updatedAt: Date,
    @ColumnInfo(name = "TRANSACTION_DATE") val transactionDate: Date?,
    @ColumnInfo(name = "TRANSACTION_REFERENCE_NO") val transactionReferenceNo: String?,
    @ColumnInfo(name = "TRANSACTION_DESCRIPTION") val transactionDescription: String?,
    @ColumnInfo(name = "TRANSACTION_TYPE") val transactionType: String?,
    @ColumnInfo(name = "TRANSACTION_BANK_NAME") val transactionBankName: String?,
    @ColumnInfo(name = "SOURCE") val source: String?,
    @ColumnInfo(name = "REMARK") val remark: String?,
    @ColumnInfo(name = "IS_VOID") val isVoid: Boolean?
) : Identifiable {
    override fun getPrimaryKeyValue(): Any = id
}
data class InvoiceReference(
    val invoiceId: Long,
    val paymentReference: String?
)
