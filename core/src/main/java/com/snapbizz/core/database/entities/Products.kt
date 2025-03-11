package com.snapbizz.core.database.entities
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snapbizz.core.database.dao.Identifiable
import java.util.Date
@Entity(tableName = "PRODUCTS")
data class Products(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "PRODUCT_CODE") val productCode: Long = 0,
    @ColumnInfo(name = "NAME") val name: String,
    @ColumnInfo(name = "MRP") val mrp: Long,
    @ColumnInfo(name = "UOM") val uom: String,
    @ColumnInfo(name = "VAT_RATE") val vatRate: Double?,
    @ColumnInfo(name = "CATEGORY_ID") val categoryId: Int?,
    @ColumnInfo(name = "IMAGE") val image: String?,
    @ColumnInfo(name = "IS_GDB") val isGdb: Boolean,
    @ColumnInfo(name = "IS_DELETED") val isDeleted: Boolean,
    @ColumnInfo(name = "CREATED_AT") val createdAt: Date,
    @ColumnInfo(name = "UPDATED_AT") val updatedAt: Date,
    @ColumnInfo(name = "HSN_CODE") val hsnCode: String?,
    @ColumnInfo(name = "SGST_RATE") val sgstRate: Double?,
    @ColumnInfo(name = "CGST_RATE") val cgstRate: Double?,
    @ColumnInfo(name = "IGST_RATE") val igstRate: Double?,
    @ColumnInfo(name = "CESS_RATE") val cessRate: Double?,
    @ColumnInfo(name = "ADDITIONAL_CESS_RATE") val additionalCessRate: Double?,
    @ColumnInfo(name = "BARCODE") val barcode: Long,
    @ColumnInfo(name = "IS_SNAP_ORDER_SYNC") val isSnapOrderSync: Boolean?,
    @ColumnInfo(name = "IS_SYNC_PENDING") var isSyncPending: Boolean?,
    @ColumnInfo(name = "IS_EXCLUSIVE_GST") val isExclusiveGst: Boolean?,
) : Identifiable {
    override fun getPrimaryKeyValue(): Any = productCode
}