package com.snapbizz.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snapbizz.core.database.dao.Identifiable
import java.util.Date

@Entity(tableName = "ITEMS")
data class Items(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val id: Long = 0,
    @ColumnInfo(name = "INVOICE_ID") val invoiceId: Long,
    @ColumnInfo(name = "NAME") val name: String?,
    @ColumnInfo(name = "PRODUCT_CODE") val productCode: Long,
    @ColumnInfo(name = "UOM") val uom: String?,
    @ColumnInfo(name = "QUANTITY") val quantity: Int,
    @ColumnInfo(name = "MRP") val mrp: Long,
    @ColumnInfo(name = "SALE_PRICE") val salePrice: Long,
    @ColumnInfo(name = "VAT_RATE") val vatRate: Double,
    @ColumnInfo(name = "VAT_AMOUNT") val vatAmount: Long,
    @ColumnInfo(name = "SAVINGS") val savings: Long,
    @ColumnInfo(name = "TOTAL_AMOUNT") val totalAmount: Long,
    @ColumnInfo(name = "CREATED_AT") val createdAt: Date,
    @ColumnInfo(name = "UPDATED_AT") val updatedAt: Date,
    @ColumnInfo(name = "HSN_CODE") val hsnCode: String?,
    @ColumnInfo(name = "SGST_RATE") var sgstRate: Double?,
    @ColumnInfo(name = "CGST_RATE") var cgstRate: Double?,
    @ColumnInfo(name = "IGST_RATE") var igstRate: Double?,
    @ColumnInfo(name = "CESS_RATE") var cessRate: Double?,
    @ColumnInfo(name = "ADDITIONAL_CESS_RATE") var additionalCessRate: Double?,
    @ColumnInfo(name = "SGST_AMOUNT") var sgstAmount: Long,
    @ColumnInfo(name = "CGST_AMOUNT") var cgstAmount: Long,
    @ColumnInfo(name = "IGST_AMOUNT") var igstAmount: Long,
    @ColumnInfo(name = "CESS_AMOUNT") var cessAmount: Long,
    @ColumnInfo(name = "ADDITIONAL_CESS_AMOUNT") var additionalCessAmount: Long,
    @ColumnInfo(name = "PURCHASE_PRICE") val purchasePrice: Long,
    @ColumnInfo(name = "CATEGORY_ID") val categoryId: Int?,
    @ColumnInfo(name = "IS_EXCLUSIVE_GST") val isExclusiveGst: Boolean?,
    @ColumnInfo(name = "FARMER_SHARE") val farmerShare: Long?,
    @ColumnInfo(name = "AGRO_CHARGE") val agroCharge: Long?,
    @ColumnInfo(name = "CONCERNED_DDO") val concernedDdo: Long?,
) : Identifiable {
    override fun getPrimaryKeyValue(): Any = id
}
