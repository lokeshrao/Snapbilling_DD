package com.snapbizz.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import com.snapbizz.core.database.dao.Identifiable
import java.util.Date

@Entity(
    tableName = "INVOICES",
    indices = [Index(value = ["CUSTOMER_PHONE"])]
)
data class Invoice(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val id: Long = 0,
    @ColumnInfo(name = "CUSTOMER_PHONE") val customerPhone: Long?,
    @ColumnInfo(name = "IS_MEMO") val isMemo: Boolean,
    @ColumnInfo(name = "TOTAL_AMOUNT") val totalAmount: Long,
    @ColumnInfo(name = "PENDING_AMOUNT") val pendingAmount: Long,
    @ColumnInfo(name = "TOTAL_DISCOUNT") val totalDiscount: Long,
    @ColumnInfo(name = "TOTAL_SAVINGS") val totalSavings: Long,
    @ColumnInfo(name = "NET_AMOUNT") val netAmount: Long,
    @ColumnInfo(name = "IS_CREDIT") val isCredit: Boolean,
    @ColumnInfo(name = "TOTAL_QUANTITY") val totalQuantity: Double,
    @ColumnInfo(name = "TOTAL_ITEMS") val totalItems: Int,
    @ColumnInfo(name = "TOTAL_VAT_AMOUNT") val totalVatAmount: Long,
    @ColumnInfo(name = "IS_DELETED") val isDeleted: Boolean,
    @ColumnInfo(name = "BILLER_NAME") val billerName: String?,
    @ColumnInfo(name = "IS_SYNC_PENDING") var isSyncPending: Boolean,
    @ColumnInfo(name = "IS_HPH_SYNC_PENDING") val isHPHSyncPending: Boolean,
    @ColumnInfo(name = "BILL_STARTED_AT") val billStartedAt: Date?,
    @ColumnInfo(name = "CREATED_AT") val createdAt: Date?,
    @ColumnInfo(name = "UPDATED_AT") val updatedAt: Date?,
    @ColumnInfo(name = "BILL_TO_PHONE") val billToPhone: Long?,
    @ColumnInfo(name = "BILL_TO_GSTIN") val billToGstin: String?,
    @ColumnInfo(name = "TOTAL_SGST_AMOUNT") val totalSgstAmount: Long?,
    @ColumnInfo(name = "TOTAL_CGST_AMOUNT") val totalCgstAmount: Long?,
    @ColumnInfo(name = "TOTAL_IGST_AMOUNT") val totalIgstAmount: Long?,
    @ColumnInfo(name = "TOTAL_CESS_AMOUNT") val totalCessAmount: Long?,
    @ColumnInfo(name = "TOTAL_ADDITIONAL_CESS_AMOUNT") val totalAdditionalCessAmount: Long?,
    @ColumnInfo(name = "IS_GST") val isGst: Boolean,
    @ColumnInfo(name = "ROUND_OFF_AMOUNT") val roundOffAmount: Long?,
    @ColumnInfo(name = "MDR_VALUE") val mdrAmount: Long?
) : Identifiable {
    override fun getPrimaryKeyValue(): Any = id
}
