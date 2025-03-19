package com.snapbizz.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "DOCTORS")
data class Doctors(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Long? = null,

    @ColumnInfo(name = "NAME")
    val name: String,

    @ColumnInfo(name = "PHONE")
    val phone: Long,

    @ColumnInfo(name = "SPECIALIST_IN")
    val specialistIn: String,

    @ColumnInfo(name = "IS_DELETED")
    val isDeleted: Boolean,

    @ColumnInfo(name = "IS_SYNC")
    val isSync: Boolean,

    @ColumnInfo(name = "CREATED_AT")
    val createdAt: Date,

    @ColumnInfo(name = "UPDATED_AT")
    val updatedAt: Date
)

