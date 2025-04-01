package com.snapbizz.core.sync.downloadSyncDto

import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.entities.Invoice
import com.snapbizz.core.database.entities.Items
import com.snapbizz.core.utils.DownSyncConfig
import java.util.Date

data class InvoiceDto(
    @SerializedName("invoice_id") val invoiceId: Long? = null,

    @SerializedName("customer_phone") val customerPhone: Long? = null,

    @SerializedName("parent_memo_id") val parentMemoId: Long? = null,

    @SerializedName("pos_name") val posName: String? = null,

    @SerializedName("is_memo") val isMemo: Boolean? = null,

    @SerializedName("is_deleted") val isDeleted: Boolean? = null,

    @SerializedName("is_delivery") val isDelivery: Boolean? = null,

    @SerializedName("is_delivered") val isDelivered: Boolean? = null,

    @SerializedName("is_credit") val isCredit: Boolean? = null,

    @SerializedName("biller_name") val billerName: String? = null,

    @SerializedName("total_items") val totalItems: Int? = null,

    @SerializedName("total_qty") val totalQuantity: Double? = null,

    @SerializedName("total_savings") val totalSavings: Long? = null,

    @SerializedName("total_discount") val totalDiscount: Long? = null,

    @SerializedName("net_amount") val netAmount: Long? = null,

    @SerializedName("total_vat_amount") val totalVatAmount: Long? = null,

    @SerializedName("total_amount") val totalAmount: Long? = null,

    @SerializedName("pending_amount") val pendingAmount: Long? = null,

    @SerializedName("delivery_charges") val deliveryCharges: Long? = null,

    @SerializedName("bill_started_at") val billStartedAt: Date? = null,

    @SerializedName("created_at") val createdAt: Date? = null,

    @SerializedName("updated_at") val updatedAt: Date? = null,

    @SerializedName("total_cgst_amount") val totalCgstAmount: Long? = null,

    @SerializedName("total_sgst_amount") val totalSgstAmount: Long? = null,

    @SerializedName("total_igst_amount") val totalIgstAmount: Long? = null,

    @SerializedName("total_cess_amount") val totalCessAmount: Long? = null,

    @SerializedName("additional_total_cess_amount") val additionalTotalCessAmount: Long? = null,

    @SerializedName("eway_number") val eWayNumber: String? = null,

    @SerializedName("bill_to_phone") val billToPhone: Long? = null,

    @SerializedName("bill_to_address") val billToAddress: String? = null,

    @SerializedName("bill_to_gstin") val billToGstin: String? = null,

    @SerializedName("ship_to_phone") val shipToPhone: Long? = null,

    @SerializedName("ship_to_address") val shipToAddress: String? = null,

    @SerializedName("ship_to_gstin") val shipToGstin: String? = null,

    @SerializedName("is_gst") val isGst: Boolean? = null,

    @SerializedName("is_split_discount_activated") val isSplitDiscountActivated: Boolean? = null,

    @SerializedName("is_edited") val isEdited: Boolean? = null,

    @SerializedName("reference_invoice_id") val referenceInvoiceId: Long? = null,

    @SerializedName("bill_edited_at") val billEditedAt: Date? = null,

    @SerializedName("is_converted_into_invoice") val isConvertedIntoInvoice: Boolean? = null,

    @SerializedName("coupon_code") val couponCode: String? = null,

    @SerializedName("coupon_amount") val couponAmount: Int? = null,

    @SerializedName("payback_amount") val paybackAmount: Int? = null,

    @SerializedName("payback_status") val paybackStatus: Boolean? = null,

    @SerializedName("snaporder_cart_id") val snapOrderCartId: Long? = null,

    @SerializedName("token_no") val tokenNo: Int? = null,

    @SerializedName("doctor_id") val doctorId: Int? = null,

    @SerializedName("round_off_amount") val roundOffAmount: Long? = null,
    @SerializedName("mdr_value") val mdrAmount: Long? = null,

    @SerializedName("items") val items: List<InvoiceItemDto>? = null
)

data class InvoiceItemDto(
    @SerializedName("product_code") val productCode: Long? = null,

    @SerializedName("name") val name: String? = null,

    @SerializedName("quantity") val quantity: Int? = null,

    @SerializedName("uom") val uom: String? = null,

    @SerializedName("measure") val measure: Int? = null,

    @SerializedName("pack_size") val packSize: Int? = null,

    @SerializedName("mrp") val mrp: Long? = null,

    @SerializedName("sale_price") val salePrice: Long? = null,

    @SerializedName("total_amount") val totalAmount: Long? = null,

    @SerializedName("savings") val savings: Long? = null,

    @SerializedName("vat_rate") val vatRate: Double? = null,

    @SerializedName("vat_amount") val vatAmount: Long? = null,

    @SerializedName("brand_discount") val brandDiscount: Long? = null,

    @SerializedName("promotion_id") val promotionId: Long? = null,

    @SerializedName("hsn_code") val hsnCode: String? = null,

    @SerializedName("gst") val gst: List<GstDetailsDto>? = null,

    @SerializedName("bill_item_discount") val billItemDiscount: Long? = null,

    @SerializedName("purchase_price") val purchasePrice: Long? = null,

    @SerializedName("category_id") val categoryId: Int? = null,

    @SerializedName("actual_profit") val actualProfit: Long? = null,

    @SerializedName("estimated_profit") val estimatedProfit: Long? = null,

    @SerializedName("is_exclusive_gst") val isExclusiveGst: Boolean? = null,

    @SerializedName("farmer_share") val farmerShare: Long? = null,

    @SerializedName("agro_charge") val agroCharge: Long? = null,

    @SerializedName("concerned_ddo") val concernedDdo: Long? = null,
)

data class GstDetailsDto(
    @SerializedName("rate") val rate: Double? = null,

    @SerializedName("amount") val amount: Long? = null,

    @SerializedName("description") val description: String? = null
)

fun invoiceDtoToEntity(apiInvoice: InvoiceDto): Pair<Invoice, List<Items>> {
    val invoice = Invoice(
        id = apiInvoice.invoiceId ?: 0,
        customerPhone = apiInvoice.customerPhone,
        isMemo = apiInvoice.isMemo ?: false,
        totalAmount = apiInvoice.totalAmount ?: 0,
        pendingAmount = apiInvoice.pendingAmount ?: 0,
        totalDiscount = apiInvoice.totalDiscount ?: 0,
        totalSavings = apiInvoice.totalSavings ?: 0,
        netAmount = apiInvoice.netAmount ?: 0,
        isCredit = apiInvoice.isCredit ?: false,
        totalQuantity = apiInvoice.totalQuantity ?: 0.0,
        totalItems = apiInvoice.totalItems ?: 0,
        totalVatAmount = apiInvoice.totalVatAmount ?: 0,
        isDeleted = apiInvoice.isDeleted ?: false,
        billerName = apiInvoice.billerName ?: "",
        isSyncPending = false,
        billStartedAt = apiInvoice.billStartedAt ?: Date(),
        createdAt = apiInvoice.createdAt ?: Date(),
        updatedAt = apiInvoice.updatedAt ?: Date(),
        billToPhone = apiInvoice.billToPhone ?: 0,
        billToGstin = apiInvoice.billToGstin ?: "",
        totalSgstAmount = apiInvoice.totalSgstAmount ?: 0,
        totalCgstAmount = apiInvoice.totalCgstAmount ?: 0,
        totalIgstAmount = apiInvoice.totalIgstAmount ?: 0,
        totalCessAmount = apiInvoice.totalCessAmount ?: 0,
        totalAdditionalCessAmount = apiInvoice.additionalTotalCessAmount ?: 0,
        isGst = apiInvoice.isGst ?: false,
        roundOffAmount = apiInvoice.roundOffAmount ?: 0,
        isHPHSyncPending = false,
        mdrAmount = apiInvoice.mdrAmount ?:0L
    )

    val items = apiInvoice.items?.map { itemDto ->
        Items(
            invoiceId = apiInvoice.invoiceId ?: 0,
            productCode = itemDto.productCode ?: 0,
            name = itemDto.name ?: "",
            quantity = itemDto.quantity ?: 0,
            uom = itemDto.uom ?: "",
            mrp = itemDto.mrp ?: 0,
            salePrice = itemDto.salePrice ?: 0,
            totalAmount = itemDto.totalAmount ?: 0,
            savings = itemDto.savings ?: 0,
            vatRate = itemDto.vatRate ?: 0.0,
            vatAmount = itemDto.vatAmount ?: 0,
            hsnCode = itemDto.hsnCode ?: "",
            purchasePrice = itemDto.purchasePrice ?: 0,
            categoryId = itemDto.categoryId ?: 0,
            isExclusiveGst =itemDto.isExclusiveGst == true,
            sgstRate = null,
            cgstRate = null,
            igstRate = null,
            cessRate = null,
            additionalCessRate = null,
            sgstAmount = 0,
            cgstAmount = 0,
            igstAmount = 0,
            cessAmount = 0,
            additionalCessAmount = 0,
            createdAt = apiInvoice.createdAt ?: Date(),
            updatedAt = apiInvoice.updatedAt ?: Date(),
            farmerShare = itemDto.farmerShare,
            agroCharge = itemDto.agroCharge,
            concernedDdo = itemDto.concernedDdo
        ).apply {
            itemDto.gst?.forEach { gstDetail ->
                when (gstDetail.description) {
                    "cgst_rate" -> {
                        cgstAmount = gstDetail.amount ?: 0
                        cgstRate = gstDetail.rate ?: 0.0
                    }

                    "igst_rate" -> {
                        igstAmount = gstDetail.amount ?: 0
                        igstRate = gstDetail.rate ?: 0.0
                    }

                    "sgst_rate" -> {
                        sgstAmount = gstDetail.amount ?: 0
                        sgstRate = gstDetail.rate ?: 0.0
                    }

                    "cess_rate" -> {
                        cessAmount = gstDetail.amount ?: 0
                        cessRate = gstDetail.rate ?: 0.0
                    }

                    "additional_cess_rate" -> {
                        additionalCessAmount = gstDetail.amount ?: 0
                        additionalCessRate = gstDetail.rate ?: 0.0
                    }
                }
            }
        }
    } ?: emptyList()

    return Pair(invoice, items)
}

fun getInvoiceSyncConfig(snapDb: SnapDatabase): DownSyncConfig<Invoice, InvoiceDto> {
    return DownSyncConfig(tableName = "invoices",
        jsonKey = "invoicesList",
        entityClass = Invoice::class.java,
        dtoClass = InvoiceDto::class.java,
        daoProvider = { snapDb.invoiceDao() },
        dtoToEntityMapper = null)
}

fun convertInvoicesAndItemsToDto(invoices: List<Invoice>, items: List<Items>): List<InvoiceDto> {
    val itemsGroupedByInvoice = items.groupBy { it.invoiceId }
    return invoices.map { invoice ->
        val relatedItems = itemsGroupedByInvoice[invoice.id] ?: emptyList()
        InvoiceDto(
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
            deliveryCharges =0,
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
            isEdited =false,
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
            mdrAmount = invoice.mdrAmount?:0,
            items = relatedItems.map { item ->
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
                    isExclusiveGst = item.isExclusiveGst?:false,
                    farmerShare = item.farmerShare?:0,
                    agroCharge = item.agroCharge?:0,
                    concernedDdo = item.concernedDdo?:0
                )
            }
        )
    }
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
