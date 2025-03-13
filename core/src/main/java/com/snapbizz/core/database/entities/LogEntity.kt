package com.snapbizz.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "LOGS")
data class LogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val source: String,
    val tag: String,
    val message: String,
    val timestamp: String
)