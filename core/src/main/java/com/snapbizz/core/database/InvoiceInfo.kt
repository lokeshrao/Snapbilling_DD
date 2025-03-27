package com.snapbizz.core.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.entities.Invoice
import com.snapbizz.core.database.entities.Items
import java.util.Date

data class InvoiceInfo(
    @ColumnInfo(name = "_id") @SerializedName("_id") val id: Long = 0,

    @ColumnInfo(name = "CUSTOMER_PHONE") @SerializedName("CUSTOMER_PHONE") val customerPhone: Long?,

    @ColumnInfo(name = "IS_MEMO") @SerializedName("IS_MEMO") val isMemo: Boolean,

    @ColumnInfo(name = "TOTAL_AMOUNT") @SerializedName("TOTAL_AMOUNT") val totalAmount: Long,

    @ColumnInfo(name = "PENDING_AMOUNT") @SerializedName("PENDING_AMOUNT") val pendingAmount: Long,

    @ColumnInfo(name = "TOTAL_DISCOUNT") @SerializedName("TOTAL_DISCOUNT") val totalDiscount: Long,

    @ColumnInfo(name = "TOTAL_SAVINGS") @SerializedName("TOTAL_SAVINGS") val totalSavings: Long,

    @ColumnInfo(name = "NET_AMOUNT") @SerializedName("NET_AMOUNT") val netAmount: Long,

    @ColumnInfo(name = "IS_CREDIT") @SerializedName("IS_CREDIT") val isCredit: Boolean,

    @ColumnInfo(name = "TOTAL_QUANTITY") @SerializedName("TOTAL_QUANTITY") val totalQuantity: Double,

    @ColumnInfo(name = "TOTAL_ITEMS") @SerializedName("TOTAL_ITEMS") val totalItems: Int,

    @ColumnInfo(name = "TOTAL_VAT_AMOUNT") @SerializedName("TOTAL_VAT_AMOUNT") val totalVatAmount: Long,

    @ColumnInfo(name = "IS_DELETED") @SerializedName("IS_DELETED") val isDeleted: Boolean,

    @ColumnInfo(name = "BILLER_NAME") @SerializedName("BILLER_NAME") val billerName: String?,

    @ColumnInfo(name = "IS_SYNC_PENDING") @SerializedName("IS_SYNC_PENDING") val isSyncPending: Boolean,

    @ColumnInfo(name = "BILL_STARTED_AT") @SerializedName("BILL_STARTED_AT") val billStartedAt: Date?,

    @ColumnInfo(name = "CREATED_AT") @SerializedName("CREATED_AT") val createdAt: Date?,

    @ColumnInfo(name = "UPDATED_AT") @SerializedName("UPDATED_AT") val updatedAt: Date?,

    @ColumnInfo(name = "BILL_TO_PHONE") @SerializedName("BILL_TO_PHONE") val billToPhone: Long?,

    @ColumnInfo(name = "BILL_TO_GSTIN") @SerializedName("BILL_TO_GSTIN") val billToGstin: String?,

    @ColumnInfo(name = "TOTAL_SGST_AMOUNT") @SerializedName("TOTAL_SGST_AMOUNT") val totalSgstAmount: Long?,

    @ColumnInfo(name = "TOTAL_CGST_AMOUNT") @SerializedName("TOTAL_CGST_AMOUNT") val totalCgstAmount: Long?,

    @ColumnInfo(name = "TOTAL_IGST_AMOUNT") @SerializedName("TOTAL_IGST_AMOUNT") val totalIgstAmount: Long?,

    @ColumnInfo(name = "TOTAL_CESS_AMOUNT") @SerializedName("TOTAL_CESS_AMOUNT") val totalCessAmount: Long?,

    @ColumnInfo(name = "TOTAL_ADDITIONAL_CESS_AMOUNT") @SerializedName("TOTAL_ADDITIONAL_CESS_AMOUNT") val totalAdditionalCessAmount: Long?,

    @ColumnInfo(name = "IS_GST") @SerializedName("IS_GST") val isGst: Boolean,

    @ColumnInfo(name = "ROUND_OFF_AMOUNT") @SerializedName("ROUND_OFF_AMOUNT") val roundOffAmount: Long?,
    @ColumnInfo(name = "MDR_VALUE") @SerializedName("MDR_VALUE") val mdrValue: Long,

    @ColumnInfo(name = "NAME") @SerializedName("NAME") var customerName: String?,

    @Relation(
        parentColumn = "_id",
        entityColumn = "INVOICE_ID"
    )
    var items: List<Items>? = mutableListOf()
)

data class InvoiceWithItems(
    @Embedded val invoice: Invoice,
    @Relation(
        parentColumn = "_id",
        entityColumn = "INVOICE_ID"
    )
    val items: List<Items>
)

