package com.snapbizz.core.database.dao.global

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.snapbizz.core.database.entities.global.Categories

@Dao
interface CategoryDao {
    @Query("SELECT * FROM CATEGORIES")
    fun getAllCategories(): List<Categories>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Categories)

    @Delete
    suspend fun delete(category: Categories)
}
