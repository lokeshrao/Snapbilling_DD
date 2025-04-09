package com.snapbizz.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snapbizz.core.database.dao.Identifiable
import java.util.Date

@Entity(tableName = "CUSTOMERS")
data class Customer(
    @PrimaryKey @ColumnInfo(name = "PHONE") val phone: Long,
    @ColumnInfo(name = "NAME") val name: String?,
    @ColumnInfo(name = "ADDRESS") val address: String?,
    @ColumnInfo(name = "EMAIL") val email: String?,
    @ColumnInfo(name = "CREDIT_LIMIT") val creditLimit: Long?,
    @ColumnInfo(name = "IS_DISABLED") val isDisabled: Boolean,
    @ColumnInfo(name = "IS_SYNC_PENDING") var isSyncPending: Boolean,
    @ColumnInfo(name = "CREATED_AT") val createdAt: Date,
    @ColumnInfo(name = "UPDATED_AT") val updatedAt: Date,
    @ColumnInfo(name = "GSTIN") val gstin: String?,
    @ColumnInfo(name = "ALT_PHONE") val altPhone: Long?,
    @ColumnInfo(name = "IS_ALT_PHONE_SELECTED") val isAltPhoneSelected: Boolean,
    @ColumnInfo(name = "IS_SNAP_ORDER_SYNC") val isSnapOrderSync: Boolean?
) : Identifiable {
    override fun getPrimaryKeyValue(): Any = phone
}

