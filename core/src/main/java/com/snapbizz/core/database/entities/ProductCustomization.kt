package com.snapbizz.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.snapbizz.core.database.dao.Identifiable
import java.util.Date

@Entity(
    tableName = "PRODUCT_CUSTOMIZATION", indices = [Index(value = ["PRODUCT_CODE"])]
)
data class ProductCustomization(
    @PrimaryKey @ColumnInfo(name = "PRODUCT_CODE") val productCode: Long,
    @ColumnInfo(name = "IS_QUICK_ADD") val isQuickAdd: Boolean,
    @ColumnInfo(name = "IS_OFFER") val isOffer: Boolean,
    @ColumnInfo(name = "IS_VISIBILITY") val isVisibility: Boolean,
    @ColumnInfo(name = "IS_SNAP_ORDER") val isSnapOrder: Boolean,
    @ColumnInfo(name = "CREATED_AT") val createdAt: Date?,
    @ColumnInfo(name = "UPDATED_AT") val updatedAt: Date?,
    @ColumnInfo(name = "IS_VISIBILITY_SYNC") val isVisibilitySync: Boolean,
    @ColumnInfo(name = "IS_SNAP_ORDER_DEAL") val isSnapOrderDeal: Boolean,
    @ColumnInfo(name = "IS_SYNC_PENDING") val isSyncPending: Boolean
) : Identifiable {
    override fun getPrimaryKeyValue(): Any = productCode
}
