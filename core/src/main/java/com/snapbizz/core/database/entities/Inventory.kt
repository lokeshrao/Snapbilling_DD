package com.snapbizz.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import com.snapbizz.core.database.dao.Identifiable
import java.util.Date

@Entity(
    tableName = "INVENTORY",
    indices = [Index(value = ["PRODUCT_CODE"])]
)
data class Inventory(
    @PrimaryKey @ColumnInfo(name = "PRODUCT_CODE") val productCode: Long,
    @ColumnInfo(name = "QUANTITY") val quantity: Int,
    @ColumnInfo(name = "MINIMUM_BASE_QUANTITY") val minimumBaseQuantity: Long?,
    @ColumnInfo(name = "RE_ORDER_POINT") val reOrderPoint: Long?,
    @ColumnInfo(name = "IS_DELETED") val isDeleted: Boolean,
    @ColumnInfo(name = "CREATED_AT") val createdAt: Date?,
    @ColumnInfo(name = "UPDATED_AT") val updatedAt: Date?,
    @ColumnInfo(name = "IS_SYNC_PENDING") var isSyncPending: Boolean
): Identifiable {
    override fun getPrimaryKeyValue(): Any = productCode
}
