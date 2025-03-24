package com.snapbizz.core.database.entities.global

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "GLOBAL_PRODUCTS",
    indices = [
        Index(value = ["PRODUCT_GID"]),
        Index(value = ["CATEGORY_ID"])
    ]
)
data class GlobalProduct(
    @PrimaryKey @ColumnInfo(name = "BARCODE") val barcode: Long,
    @ColumnInfo(name = "PRODUCT_GID") val productGid: Int?,
    @ColumnInfo(name = "NAME") val name: String,
    @ColumnInfo(name = "MRP") val mrp: Long,
    @ColumnInfo(name = "ALTERNATE_MRP") val alternateMrp: String?,
    @ColumnInfo(name = "UOM") val uom: String,
    @ColumnInfo(name = "MEASURE") val measure: Int,
    @ColumnInfo(name = "BRAND_ID") val brandId: Int,
    @ColumnInfo(name = "CATEGORY_ID") val categoryId: Int,
    @ColumnInfo(name = "VAT_ID") val vatId: Int?,
    @ColumnInfo(name = "IS_DISABLED") val isDisabled: Boolean,
    @ColumnInfo(name = "IMAGE") val image: String?,
    @ColumnInfo(name = "LOCAL_NAME") val localName: String?,
    @ColumnInfo(name = "IS_PC") val isPc: Boolean,
    @ColumnInfo(name = "IS_MYSTORE") val isMyStore: Boolean,
    @ColumnInfo(name = "IS_GDB_MYSTORE") val isGdbMyStore: Boolean,
    @ColumnInfo(name = "HSN_CODE") val hsnCode: String?,
    @ColumnInfo(name = "GST_ID") val gstId: Int?,
    @ColumnInfo(name = "CREATED_AT") val createdAt: Long,
    @ColumnInfo(name = "UPDATED_AT") val updatedAt: Long,
    @ColumnInfo(name = "RATING") val rating: Int?
)