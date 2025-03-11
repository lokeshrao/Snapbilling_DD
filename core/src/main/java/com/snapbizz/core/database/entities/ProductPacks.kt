package com.snapbizz.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import com.snapbizz.core.database.dao.Identifiable
import java.util.Date

@Entity(
    tableName = "PRODUCT_PACKS",
    indices = [Index(value = ["PRODUCT_CODE","SEARCH_DATA"])]
)
data class ProductPacks(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") var id: Long? = 0,
    @ColumnInfo(name = "PRODUCT_CODE") val productCode: Long,
    @ColumnInfo(name = "PACK_SIZE") val packSize: Int?,
    @ColumnInfo(name = "SALE_PRICE1") val salePrice1: Long,
    @ColumnInfo(name = "SALE_PRICE2") val salePrice2: Long,
    @ColumnInfo(name = "SALE_PRICE3") val salePrice3: Long,
    @ColumnInfo(name = "PURCHASE_PRICE") val purchasePrice: Long,
    @ColumnInfo(name = "IS_DEFAULT") val isDefault: Boolean,
    @ColumnInfo(name = "IS_DELETED") val isDeleted: Boolean,
    @ColumnInfo(name = "CREATED_AT") val createdAt: Date?,
    @ColumnInfo(name = "UPDATED_AT") val updatedAt: Date?,
    @ColumnInfo(name = "BARCODE") val barcode: Long?,
    @ColumnInfo(name = "MRP") val mrp: Long?,
    @ColumnInfo(name = "IS_SNAP_ORDER_SYNC") val isSnapOrderSync: Boolean?,
    @ColumnInfo(name = "EXP_DATE") val expDate: Date?,
    @ColumnInfo(name = "IS_SYNC_PENDING") var isSyncPending: Boolean,
    @ColumnInfo(name = "FARMER_SHARE") val farmerShare :Long?=null,
    @ColumnInfo(name = "SEARCH_DATA") val search :String?=null,
) : Identifiable {
    override fun getPrimaryKeyValue(): Any = id?:0
}