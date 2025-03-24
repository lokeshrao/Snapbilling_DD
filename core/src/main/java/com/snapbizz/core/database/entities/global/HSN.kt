package com.snapbizz.core.database.entities.global

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HSN")
data class Hsn(
    @PrimaryKey @ColumnInfo(name = "HSN_CODE") val hsnCode: String,
    @ColumnInfo(name = "GST_ID") val gstId: Int,
    @ColumnInfo(name = "CREATED_AT") val createdAt: Long,
    @ColumnInfo(name = "UPDATED_AT") val updatedAt: Long
)
