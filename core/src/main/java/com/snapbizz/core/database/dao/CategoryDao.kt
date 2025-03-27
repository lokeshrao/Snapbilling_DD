package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.snapbizz.core.database.entities.Category

@Dao
interface CategoryDao{

    @Query("DELETE FROM CATEGORIES")
    fun deleteAll()

    @Query("SELECT * FROM CATEGORIES")
    suspend fun getAllCategories():List<Category>

    @Query("SELECT * FROM CATEGORIES WHERE PARENT_ID > 0")
    suspend fun getAllSubCategories():List<Category>

    @Query("SELECT * FROM CATEGORIES WHERE PARENT_ID > 0 LIMIT 15")
    suspend fun getQuickAddCategories():List<Category>
}
