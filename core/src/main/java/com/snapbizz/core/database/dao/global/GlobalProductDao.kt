package com.snapbizz.core.database.dao.global

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.snapbizz.core.database.entities.global.GlobalProduct

@Dao
interface GlobalProductDao {
    @Query("SELECT * FROM GLOBAL_PRODUCTS WHERE CATEGORY_ID = :categoryId")
    fun getProductsByCategory(categoryId: Int): List<GlobalProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: GlobalProduct)

    @Query("SELECT * FROM GLOBAL_PRODUCTS WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun getProducts(query: String): PagingSource<Int, GlobalProduct>

    @Delete
    suspend fun deleteProduct(product: GlobalProduct)
}
