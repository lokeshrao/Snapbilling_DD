package com.snapbizz.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snapbizz.core.database.dao.Identifiable
import java.util.Date

@Entity(tableName = "APPOINTMENTS")
data class Appointments(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "APPOINTMENT_ID")
    val appointmentId: Long? = null,

    @ColumnInfo(name = "CUSTOMER_NUMBER")
    val customerNumber: Long,

    @ColumnInfo(name = "REPRESENTATIVE_ID")
    val representativeId: Long,

    @ColumnInfo(name = "START_DATE")
    val startDate: Date,

    @ColumnInfo(name = "END_DATE")
    val endDate: Date,

    @ColumnInfo(name = "DESCRIPTION")
    val description: String,

    @ColumnInfo(name = "IS_DELETED")
    val isDeleted: Boolean,

    @ColumnInfo(name = "STATUS")
    val status: String,

    @ColumnInfo(name = "CANCELLATION_REASON")
    val cancellationReason: String?,

    @ColumnInfo(name = "IS_SYNC_PENDING")
    val isSyncPending: Boolean,

    @ColumnInfo(name = "CREATED_AT")
    val createdAt: Date,

    @ColumnInfo(name = "UPDATED_AT")
    val updatedAt: Date
): Identifiable {
    override fun getPrimaryKeyValue(): Any = appointmentId?:0
}

