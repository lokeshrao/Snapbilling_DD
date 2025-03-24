package com.snapbizz.core.database.entities.global

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "VAT")
data class Vat(
    @PrimaryKey @ColumnInfo(name = "_id") val id: Int,
    @ColumnInfo(name = "VAT_RATE") val vatRate: Double?,
    @ColumnInfo(name = "CREATED_AT") val createdAt: Long,
    @ColumnInfo(name = "UPDATED_AT") val updatedAt: Long
)
