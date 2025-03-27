package com.snapbizz.core.database.dao
import androidx.room.Dao
import androidx.room.Query
import com.snapbizz.core.database.entities.Products

@Dao
interface ProductsDao : GenericDao<Products> {

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