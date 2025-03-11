package com.snapbizz.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "USERS")
data class Users(
    @PrimaryKey @ColumnInfo(name = "USERNAME") val username: String,
    @ColumnInfo(name = "PASSWORD") val password: String?,
    @ColumnInfo(name = "NAME") val name: String?,
    @ColumnInfo(name = "ROLE_ID") val roleId: Int,
    @ColumnInfo(name = "IS_DISABLED") val isDisabled: Boolean,
    @ColumnInfo(name = "CREATED_AT") val createdAt: Date?,
    @ColumnInfo(name = "UPDATED_AT") val updatedAt: Date?
)
