package com.snapbizz.core.database.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.snapbizz.core.database.entities.ProductCustomization

@Dao
interface ProductCustomizationDao : GenericDao<ProductCustomization> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productCustomization: ProductCustomization): Long

    @Update
    suspend fun update(productCustomization: ProductCustomization)

    @Query("SELECT * FROM PRODUCT_CUSTOMIZATION WHERE PRODUCT_CODE = :productCode")
    suspend fun getProductCustomizationByCode(productCode: Long): ProductCustomization?

    @Query("DELETE FROM PRODUCT_CUSTOMIZATION WHERE PRODUCT_CODE = :productCode")
    suspend fun deleteByProductCode(productCode: Long)

    @Query("SELECT * FROM PRODUCT_CUSTOMIZATION")
    suspend fun getAllProductCustomizations(): List<ProductCustomization>

    @Query("DELETE FROM PRODUCT_CUSTOMIZATION")
    suspend fun deleteAll()
}
