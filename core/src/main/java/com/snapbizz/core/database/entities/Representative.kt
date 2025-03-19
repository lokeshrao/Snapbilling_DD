package com.snapbizz.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "REPRESENTATIVE")
data class Representative(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "REPRESENTATIVE_ID")
    val representativeId: Long? = null,

    @ColumnInfo(name = "NAME")
    val name: String,

    @ColumnInfo(name = "PHONE")
    val phone: Long,

    @ColumnInfo(name = "IS_DELETED")
    val isDeleted: Boolean,

    @ColumnInfo(name = "IS_SYNC")
    val isSync: Boolean,

    @ColumnInfo(name = "CREATED_AT")
    val createdAt: Date,

    @ColumnInfo(name = "UPDATED_AT")
    val updatedAt: Date
)

