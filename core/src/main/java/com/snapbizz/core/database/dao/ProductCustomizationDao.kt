package com.snapbizz.core.database.dao
import androidx.room.Dao
import androidx.room.Query
import com.snapbizz.core.database.entities.ProductCustomization

@Dao
interface ProductCustomizationDao : GenericDao<ProductCustomization> {

    @Query("SELECT * FROM PRODUCT_CUSTOMIZATION WHERE PRODUCT_CODE = :productCode")
    suspend fun getProductCustomizationByCode(productCode: Long): ProductCustomization?

    @Query("DELETE FROM PRODUCT_CUSTOMIZATION WHERE PRODUCT_CODE = :productCode")
    suspend fun deleteByProductCode(productCode: Long)

    @Query("SELECT * FROM PRODUCT_CUSTOMIZATION")
    suspend fun getAllProductCustomizations(): List<ProductCustomization>

    @Query("DELETE FROM PRODUCT_CUSTOMIZATION")
    suspend fun deleteAll()
}
