package com.snapbizz.core.database.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.snapbizz.core.database.entities.Products

@Dao
interface ProductsDao : GenericDao<Products> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Products): Long

    @Update
    suspend fun update(product: Products)

    @Query("SELECT * FROM PRODUCTS WHERE PRODUCT_CODE = :id")
    suspend fun getProductById(id: Long): Products?

    @Query("DELETE FROM PRODUCTS WHERE PRODUCT_CODE = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM PRODUCTS")
    suspend fun getAllProducts(): List<Products>

    @Query("DELETE FROM PRODUCTS")
    fun deleteAll()

}
interface Identifiable {
    fun getPrimaryKeyValue(): Any
}