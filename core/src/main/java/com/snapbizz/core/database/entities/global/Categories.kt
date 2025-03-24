package com.snapbizz.core.database.entities.global

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CATEGORIES")
data class Categories(
    @PrimaryKey @ColumnInfo(name = "_id") val id: Int,
    @ColumnInfo(name = "NAME") val name: String,
    @ColumnInfo(name = "PARENT_ID") val parentId: Int?,
    @ColumnInfo(name = "VAT_ID") val vatId: Int,
    @ColumnInfo(name = "IS_QUICK_ADD") val isQuickAdd: Boolean,
    @ColumnInfo(name = "CREATED_AT") val createdAt: Long,
    @ColumnInfo(name = "UPDATED_AT") val updatedAt: Long,
    @ColumnInfo(name = "MARGIN") val margin: Double?
)
