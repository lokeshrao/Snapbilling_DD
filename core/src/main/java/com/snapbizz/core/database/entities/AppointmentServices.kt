package com.snapbizz.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "APPOINTMENT_SERVICES")
data class AppointmentServices(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Long? = null,

    @ColumnInfo(name = "APPOINTMENT_ID")
    val appointmentId: Long,

    @ColumnInfo(name = "SERVICE_NAME")
    val serviceName: String,

    @ColumnInfo(name = "SERVICE_PRODUCT_CODE")
    val serviceProductCode: Long,

    @ColumnInfo(name = "CREATED_AT")
    val createdAt: Date,

    @ColumnInfo(name = "UPDATED_AT")
    val updatedAt: Date
)


