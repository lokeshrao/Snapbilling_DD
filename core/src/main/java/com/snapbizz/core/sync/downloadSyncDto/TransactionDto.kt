package com.snapbizz.core.sync.downloadSyncDto

import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.entities.Transactions
import com.snapbizz.core.utils.DownSyncConfig
import java.util.Date

data class TransactionDto(
    @SerializedName("transaction_id") val transactionId: Long,
    @SerializedName("customer_phone") val customerPhone: Long?,
    @SerializedName("invoice_id") val invoiceId: Long?,
    @SerializedName("payment_type") val paymentType: String?,
    @SerializedName("payment_mode") val paymentMode: String?,
    @SerializedName("paid_amount") val paidAmount: Long?,
    @SerializedName("mdr_value") val mdrAmount: Long?,
    @SerializedName("remaining_amount") val remainingAmount: Long?,
    @SerializedName("tendered_amount") val tenderedAmount: Long?,
    @SerializedName("parent_transaction_id") val parentTransactionId: Long?,
    @SerializedName("payment_reference") val paymentReference: String?,
    @SerializedName("created_at") val createdAt: Date?,
    @SerializedName("updated_at") val updatedAt: Date?,
    @SerializedName("transaction_date") val transactionDate: Date?,
    @SerializedName("transaction_ref_no") val transactionRefNo: String?,
    @SerializedName("transaction_desc") val transactionDesc: String?,
    @SerializedName("transaction_type") val transactionType: String?,
    @SerializedName("transaction_bank_name") val transactionBankName: String?,
    @SerializedName("source") val source: String?,
    @SerializedName("remark") val remark: String?,
    @SerializedName("is_void") val isVoid: Boolean
)

fun transactionDtoToEntity(apiTransaction: TransactionDto): Transactions {
    return Transactions(
        id = apiTransaction.transactionId,
        invoiceId = apiTransaction.invoiceId ?: 0,
        paymentType = apiTransaction.paymentType ?: "",
        paymentMode = apiTransaction.paymentMode ?: "",
        paymentReference = apiTransaction.paymentReference ?: "",
        amount = apiTransaction.paidAmount ?: 0,
        customerPhone = apiTransaction.customerPhone ?: 0,
        parentTransactionId = apiTransaction.parentTransactionId ?: 0,
        isSyncPending = false,
        createdAt = apiTransaction.createdAt ?: Date(),
        updatedAt = apiTransaction.updatedAt ?: Date(),
        transactionDate = apiTransaction.transactionDate ?: apiTransaction.createdAt ?: Date(),
        transactionReferenceNo = apiTransaction.transactionRefNo ?: "",
        transactionDescription = apiTransaction.transactionDesc ?: "",
        isVoid = apiTransaction.isVoid,
        transactionType = apiTransaction.transactionType ?: "",
        transactionBankName = apiTransaction.transactionBankName ?: "",
        source = apiTransaction.source ?: "",
        remark = apiTransaction.remark ?: "",
        mdrAmount = apiTransaction.mdrAmount ?: 0
    )
}

fun getTransactionSyncConfig(snapDb: SnapDatabase): DownSyncConfig<Transactions, TransactionDto> {
    return DownSyncConfig(
        tableName = "transactions",
        jsonKey = "invoice_transactionsList",
        entityClass = Transactions::class.java,
        dtoClass = TransactionDto::class.java,
        daoProvider = { snapDb.transactionsDao() },
        dtoToEntityMapper = ::transactionDtoToEntity
    )
}

fun transactionToEntity(transaction: Transactions?): TransactionDto {
    return TransactionDto(
        transaction?.id ?: 0L,
        transaction?.customerPhone,
        transaction?.invoiceId,
        transaction?.paymentType,
        transaction?.paymentMode,
        transaction?.amount,
        transaction?.mdrAmount,
        0,
        0,
        transaction?.parentTransactionId,
        transaction?.paymentReference,
        transaction?.createdAt,
        transaction?.updatedAt,
        transaction?.transactionDate,
        transaction?.transactionReferenceNo,
        transaction?.transactionDescription,
        transaction?.transactionType,
        transaction?.transactionBankName,
        transaction?.source,
        transaction?.remark,
        transaction?.isVoid ?: false
    )
}
