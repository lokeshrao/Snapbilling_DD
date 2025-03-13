package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.snapbizz.core.database.entities.LogEntity

@Dao
interface LogDao {
    @Insert
    suspend fun insertLog(log: LogEntity)

    @Query("SELECT * FROM logs ORDER BY timestamp DESC")
    suspend fun getAllLogs(): List<LogEntity>
}