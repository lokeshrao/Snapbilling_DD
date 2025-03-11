package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.snapbizz.core.database.entities.Category

@Dao
interface CategoryDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: List<Category>)

    @Query("DELETE FROM CATEGORIES")
    fun deleteAll()

    @Query("SELECT * FROM CATEGORIES")
    suspend fun getAllCategories():List<Category>

    @Query("SELECT * FROM CATEGORIES WHERE PARENT_ID > 0")
    suspend fun getAllSubCategories():List<Category>

    @Query("SELECT * FROM CATEGORIES WHERE PARENT_ID > 0 LIMIT 15")
    suspend fun getQuickAddCategories():List<Category>
}
