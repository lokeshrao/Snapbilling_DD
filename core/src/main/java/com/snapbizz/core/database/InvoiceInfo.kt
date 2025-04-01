package com.snapbizz.core.database

import androidx.room.ColumnInfo
import androidx.room.Relation
import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.entities.InvoiceWithItems
import com.snapbizz.core.database.entities.Items
import com.snapbizz.core.sync.downloadSyncDto.GstDetailsDto
import com.snapbizz.core.sync.downloadSyncDto.InvoiceDto
import com.snapbizz.core.sync.downloadSyncDto.InvoiceItemDto
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

fun convertInvoiceWithItemsToDto(invoiceWithItems: InvoiceWithItems): InvoiceDto {
    val invoice = invoiceWithItems.invoice
    val items = invoiceWithItems.items

    return InvoiceDto(
        invoiceId = invoice.id,
        customerPhone = invoice.customerPhone,
        parentMemoId = null,
        posName = null,
        isMemo = invoice.isMemo,
        isDeleted = invoice.isDeleted,
        isDelivery = false,
        isDelivered = false,
        isCredit = invoice.isCredit,
        billerName = invoice.billerName,
        totalItems = invoice.totalItems,
        totalQuantity = invoice.totalQuantity,
        totalSavings = invoice.totalSavings,
        totalDiscount = invoice.totalDiscount,
        netAmount = invoice.netAmount,
        totalVatAmount = invoice.totalVatAmount,
        totalAmount = invoice.totalAmount,
        pendingAmount = invoice.pendingAmount,
        deliveryCharges = 0,
        billStartedAt = invoice.billStartedAt,
        createdAt = invoice.createdAt,
        updatedAt = invoice.updatedAt,
        totalCgstAmount = invoice.totalCgstAmount,
        totalSgstAmount = invoice.totalSgstAmount,
        totalIgstAmount = invoice.totalIgstAmount,
        totalCessAmount = invoice.totalCessAmount,
        additionalTotalCessAmount = invoice.totalAdditionalCessAmount,
        eWayNumber = null,
        billToPhone = invoice.billToPhone,
        billToAddress = null,
        billToGstin = invoice.billToGstin,
        shipToPhone = null,
        shipToAddress = null,
        shipToGstin = null,
        isGst = invoice.isGst,
        isSplitDiscountActivated = false,
        isEdited = false,
        referenceInvoiceId = null,
        billEditedAt = null,
        isConvertedIntoInvoice = false,
        couponCode = null,
        couponAmount = null,
        paybackAmount = null,
        paybackStatus = null,
        snapOrderCartId = null,
        tokenNo = null,
        doctorId = null,
        roundOffAmount = invoice.roundOffAmount,
        mdrAmount = invoice.mdrAmount ?: 0,
        items = items.map { item ->
            InvoiceItemDto(
                productCode = item.productCode,
                name = item.name,
                quantity = item.quantity,
                uom = item.uom,
                measure = 0,
                packSize = 0,
                mrp = item.mrp,
                salePrice = item.salePrice,
                totalAmount = item.totalAmount,
                savings = item.savings,
                vatRate = item.vatRate,
                vatAmount = item.vatAmount,
                brandDiscount = 0,
                promotionId = null,
                hsnCode = item.hsnCode,
                gst = getGSTDetailsFromItems(item),
                billItemDiscount = 0,
                purchasePrice = item.purchasePrice,
                categoryId = item.categoryId,
                actualProfit = 0,
                estimatedProfit = 0,
                isExclusiveGst = item.isExclusiveGst ?: false,
                farmerShare = item.farmerShare ?: 0,
                agroCharge = item.agroCharge ?: 0,
                concernedDdo = item.concernedDdo ?: 0
            )
        }
    )
}
private fun getGSTDetailsFromItems(items: Items): List<GstDetailsDto> {
    val CGST_RATE_TAG = "cgst_rate"
    val IGST_RATE_TAG = "igst_rate"
    val SGST_RATE_TAG = "sgst_rate"
    val CESS_RATE_TAG = "cess_rate"
    val ADDITIONAL_CESS_TAG = "additional_cess_rate"
    return listOfNotNull(
        items.cgstAmount.let { amount ->
            items.cgstRate?.let { rate ->
                GstDetailsDto(description = CGST_RATE_TAG, rate = rate, amount = amount)
            }
        },
        items.sgstAmount.let { amount ->
            items.sgstRate?.let { rate ->
                GstDetailsDto(description = SGST_RATE_TAG, rate = rate, amount = amount)
            }
        },
        items.igstAmount.let { amount ->
            items.igstRate?.let { rate ->
                GstDetailsDto(description = IGST_RATE_TAG, rate = rate, amount = amount)
            }
        },
        items.cessAmount.let { amount ->
            items.cessRate?.let { rate ->
                GstDetailsDto(description = CESS_RATE_TAG, rate = rate, amount = amount)
            }
        },
        items.additionalCessAmount.let { amount ->
            items.additionalCessRate?.let { rate ->
                GstDetailsDto(description = ADDITIONAL_CESS_TAG, rate = rate.toDouble(), amount = amount)
            }
        }
    )
}



