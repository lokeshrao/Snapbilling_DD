package com.snapbizz.core.database.entities.global

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "GST",
    indices = [Index(value = ["GST_ID", "STATE_ID"], name = "IDX_GST_GST_ID_STATE_ID", unique = true)]
)data class Gst(
    @PrimaryKey @ColumnInfo(name = "_id") val id: Int,
    @ColumnInfo(name = "GST_ID") val gstId: Int,
    @ColumnInfo(name = "STATE_ID") val stateId: Int,
    @ColumnInfo(name = "CGST") val cgst: Double?,
    @ColumnInfo(name = "SGST") val sgst: Double?,
    @ColumnInfo(name = "IGST") val igst: Double?,
    @ColumnInfo(name = "CESS") val cess: Double?,
    @ColumnInfo(name = "ADDITIONAL_CESS") val additionalCess: Double?,
    @ColumnInfo(name = "CREATED_AT") val createdAt: Long,
    @ColumnInfo(name = "UPDATED_AT") val updatedAt: Long
)
