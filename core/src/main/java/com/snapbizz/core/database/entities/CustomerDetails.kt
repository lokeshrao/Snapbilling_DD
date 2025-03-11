package com.snapbizz.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snapbizz.core.database.dao.Identifiable
import java.util.Date

@Entity(tableName = "CUSTOMER_DETAILS")
data class CustomerDetails(
    @PrimaryKey @ColumnInfo(name = "PHONE") val phone: Long,
    @ColumnInfo(name = "AMOUNT_DUE") val amountDue: Long,
    @ColumnInfo(name = "PURCHASE_VALUE") var purchaseValue: Long,
    @ColumnInfo(name = "AMOUNT_SAVED") val amountSaved: Long,
    @ColumnInfo(name = "LAST_PURCHASE_AMOUNT") var lastPurchaseAmount: Long?,
    @ColumnInfo(name = "LAST_PAYMENT_AMOUNT") var lastPaymentAmount: Long?,
    @ColumnInfo(name = "TOTAL_VISITS") var totalVisits: Int,
    @ColumnInfo(name = "LAST_PURCHASE_DATE") var lastPurchaseDate: Date?,
    @ColumnInfo(name = "LAST_PAYMENT_DATE") var lastPaymentDate: Date?,
    @ColumnInfo(name = "CREATED_AT") val createdAt: Date,
    @ColumnInfo(name = "UPDATED_AT") var updatedAt: Date,
    @ColumnInfo(name = "REWARD_POINTS") val rewardPoints: Int,
    @ColumnInfo(name = "IS_SYNC_PENDING") var isSyncPending: Boolean?
)  : Identifiable {
    override fun getPrimaryKeyValue(): Any = phone
}