package com.snapbizz.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.snapbizz.common.models.ProductInfo
import com.snapbizz.core.database.entities.ProductPacks
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductPacksDao : GenericDao<ProductPacks> {

    @Query("SELECT * FROM PRODUCT_PACKS WHERE _id = :id")
    suspend fun getProductPackById(id: Long): ProductPacks?

    @Query("SELECT * FROM PRODUCT_PACKS WHERE PRODUCT_CODE = :productCode")
    fun getProductPacksByProductCode(productCode: Long): Flow<List<ProductPacks>>

    @Query("DELETE FROM PRODUCT_PACKS")
    fun deleteAll()

    @Query("UPDATE PRODUCT_PACKS SET SEARCH_DATA = :searchData WHERE PRODUCT_CODE = :productCode")
    suspend fun updateSearchData(productCode: Long, searchData: String)


    @Query(
        """
    SELECT
         p.PRODUCT_CODE AS productCode,
        p.NAME AS name,
        COALESCE(pp.MRP, 0) AS mrp,
        p.UOM AS uom,
        COALESCE(p.VAT_RATE, 0.0) AS vatRate,
        COALESCE(p.CATEGORY_ID, 0) AS categoryId,
        COALESCE(p.IMAGE, '') AS image,
        COALESCE(p.IS_GDB, 0) AS isGdb,
        COALESCE(p.IS_DELETED, 0) AS isDeleted,
        p.CREATED_AT AS createdAt,
        p.UPDATED_AT AS updatedAt,
        COALESCE(p.HSN_CODE, '') AS hsnCode,
        COALESCE(p.SGST_RATE, 0.0) AS sgstRate,
        COALESCE(p.CGST_RATE, 0.0) AS cgstRate,
        COALESCE(p.IGST_RATE, 0.0) AS igstRate,
        COALESCE(p.CESS_RATE, 0.0) AS cessRate,
        COALESCE(p.ADDITIONAL_CESS_RATE, 0.0) AS additionalCessRate,
        COALESCE(p.BARCODE, 0) AS barcode,
        COALESCE(p.IS_SNAP_ORDER_SYNC, 0) AS isSnapOrderSync,
        COALESCE(p.IS_SYNC_PENDING, 0) AS isSyncPending,
        COALESCE(p.IS_EXCLUSIVE_GST, 0) AS isExclusiveGst,
        COALESCE(pp.PACK_SIZE, 0) AS packSize,
        COALESCE(pp.SALE_PRICE1, 0) AS salePrice1,
        COALESCE(pp.SALE_PRICE2, 0) AS salePrice2,
        COALESCE(pp.SALE_PRICE3, 0) AS salePrice3,
        COALESCE(pp.PURCHASE_PRICE, 0) AS purchasePrice,
        COALESCE(pp.IS_DEFAULT, 0) AS isDefault,
        COALESCE(pp.BARCODE, 0) AS packBarcode,
        pp.EXP_DATE AS expDate,
        COALESCE(i.QUANTITY, 0) AS inventoryQuantity,
        COALESCE(i.MINIMUM_BASE_QUANTITY, 0) AS minimumBaseQuantity,
        COALESCE(i.RE_ORDER_POINT, 0) AS reOrderPoint
FROM PRODUCT_PACKS pp
    LEFT JOIN PRODUCTS p ON pp.PRODUCT_CODE = p.PRODUCT_CODE
    LEFT JOIN INVENTORY i ON p.PRODUCT_CODE = i.PRODUCT_CODE
    WHERE (:query IS NULL OR pp.SEARCH_DATA LIKE '%' || :query || '%')
    ORDER BY p.PRODUCT_CODE
"""
    )
    fun searchProducts(query: String?): PagingSource<Int, ProductInfo>


//
//    @Query(
//        """
//    SELECT
//         p.PRODUCT_CODE AS productCode,
//        p.NAME AS name,
//        COALESCE(pp.MRP, 0) AS mrp,
//        p.UOM AS uom,
//        COALESCE(p.VAT_RATE, 0.0) AS vatRate,
//        COALESCE(p.CATEGORY_ID, 0) AS categoryId,
//        COALESCE(p.IMAGE, '') AS image,
//        COALESCE(p.IS_GDB, 0) AS isGdb,
//        COALESCE(p.IS_DELETED, 0) AS isDeleted,
//        p.CREATED_AT AS createdAt,
//        p.UPDATED_AT AS updatedAt,
//        COALESCE(p.HSN_CODE, '') AS hsnCode,
//        COALESCE(p.SGST_RATE, 0.0) AS sgstRate,
//        COALESCE(p.CGST_RATE, 0.0) AS cgstRate,
//        COALESCE(p.IGST_RATE, 0.0) AS igstRate,
//        COALESCE(p.CESS_RATE, 0.0) AS cessRate,
//        COALESCE(p.ADDITIONAL_CESS_RATE, 0.0) AS additionalCessRate,
//        COALESCE(p.BARCODE, 0) AS barcode,
//        COALESCE(p.IS_SNAP_ORDER_SYNC, 0) AS isSnapOrderSync,
//        COALESCE(p.IS_SYNC_PENDING, 0) AS isSyncPending,
//        COALESCE(p.IS_EXCLUSIVE_GST, 0) AS isExclusiveGst,
//        COALESCE(pp.PACK_SIZE, 0) AS packSize,
//        COALESCE(pp.SALE_PRICE1, 0) AS salePrice1,
//        COALESCE(pp.SALE_PRICE2, 0) AS salePrice2,
//        COALESCE(pp.SALE_PRICE3, 0) AS salePrice3,
//        COALESCE(pp.PURCHASE_PRICE, 0) AS purchasePrice,
//        COALESCE(pp.IS_DEFAULT, 0) AS isDefault,
//        COALESCE(pp.BARCODE, 0) AS packBarcode,
//        pp.EXP_DATE AS expDate,
//        COALESCE(i.QUANTITY, 0) AS inventoryQuantity,
//        COALESCE(i.MINIMUM_BASE_QUANTITY, 0) AS minimumBaseQuantity,
//        COALESCE(i.RE_ORDER_POINT, 0) AS reOrderPoint,
//        COALESCE(pc.IS_QUICK_ADD, 0) AS isQuickAdd,
//        COALESCE(pp.FARMER_SHARE, 0) AS farmerShare
//
//FROM PRODUCT_PACKS pp
//    LEFT JOIN PRODUCTS p ON pp.PRODUCT_CODE = p.PRODUCT_CODE
//    LEFT JOIN INVENTORY i ON p.PRODUCT_CODE = i.PRODUCT_CODE
//    LEFT JOIN PRODUCT_CUSTOMIZATION pc ON pc.PRODUCT_CODE = pp.PRODUCT_CODE
//    WHERE pc.IS_QUICK_ADD = 1
//    ORDER BY p.PRODUCT_CODE
//"""
//    )
//    suspend fun getQuickAddProducts(
//    ): List<ProductInfo>

    @Query(
        """
    SELECT COUNT(*)
    FROM (
        SELECT p.PRODUCT_CODE
        FROM PRODUCT_PACKS pp
        LEFT JOIN PRODUCTS p ON pp.PRODUCT_CODE = p.PRODUCT_CODE
        LEFT JOIN INVENTORY i ON p.PRODUCT_CODE = i.PRODUCT_CODE
        WHERE (:query IS NULL OR pp.search_data LIKE '%' || :query || '%')
        AND (:categoryId IS NULL OR p.CATEGORY_ID = :categoryId)
    ) AS filteredProducts
"""
    )
    suspend fun getProductCount(query: String?, categoryId: Int?): Int

    @Query(
        """
         UPDATE PRODUCT_PACKS
        SET search_data = (
            SELECT p.NAME || ' ' || p.BARCODE || ' ' || p.MRP
            FROM PRODUCTS p
            WHERE p.PRODUCT_CODE = PRODUCT_PACKS.PRODUCT_CODE
        )
        WHERE EXISTS (
            SELECT 1
            FROM PRODUCTS p
            WHERE p.PRODUCT_CODE = PRODUCT_PACKS.PRODUCT_CODE
        )
    """
    )
    suspend fun updateSearchData()

//    @Query(
//        """
//    SELECT
//         p.PRODUCT_CODE AS productCode,
//        p.NAME AS name,
//        COALESCE(pp.MRP, 0) AS mrp,
//        p.UOM AS uom,
//        COALESCE(p.VAT_RATE, 0.0) AS vatRate,
//        COALESCE(p.CATEGORY_ID, 0) AS categoryId,
//        COALESCE(p.IMAGE, '') AS image,
//        COALESCE(p.IS_GDB, 0) AS isGdb,
//        COALESCE(p.IS_DELETED, 0) AS isDeleted,
//        p.CREATED_AT AS createdAt,
//        p.UPDATED_AT AS updatedAt,
//        COALESCE(p.HSN_CODE, '') AS hsnCode,
//        COALESCE(p.SGST_RATE, 0.0) AS sgstRate,
//        COALESCE(p.CGST_RATE, 0.0) AS cgstRate,
//        COALESCE(p.IGST_RATE, 0.0) AS igstRate,
//        COALESCE(p.CESS_RATE, 0.0) AS cessRate,
//        COALESCE(p.ADDITIONAL_CESS_RATE, 0.0) AS additionalCessRate,
//        COALESCE(p.BARCODE, 0) AS barcode,
//        COALESCE(p.IS_SNAP_ORDER_SYNC, 0) AS isSnapOrderSync,
//        COALESCE(p.IS_SYNC_PENDING, 0) AS isSyncPending,
//        COALESCE(p.IS_EXCLUSIVE_GST, 0) AS isExclusiveGst,
//        COALESCE(pp.PACK_SIZE, 0) AS packSize,
//        COALESCE(pp.SALE_PRICE1, 0) AS salePrice1,
//        COALESCE(pp.SALE_PRICE2, 0) AS salePrice2,
//        COALESCE(pp.SALE_PRICE3, 0) AS salePrice3,
//        COALESCE(pp.PURCHASE_PRICE, 0) AS purchasePrice,
//        COALESCE(pp.IS_DEFAULT, 0) AS isDefault,
//        COALESCE(pp.BARCODE, 0) AS packBarcode,
//        pp.EXP_DATE AS expDate,
//        COALESCE(i.QUANTITY, 0) AS inventoryQuantity,
//        COALESCE(i.MINIMUM_BASE_QUANTITY, 0) AS minimumBaseQuantity,
//        COALESCE(i.RE_ORDER_POINT, 0) AS reOrderPoint,
//        COALESCE(pp.FARMER_SHARE, 0) AS farmerShare
//FROM PRODUCT_PACKS pp
//    LEFT JOIN PRODUCTS p ON pp.PRODUCT_CODE = p.PRODUCT_CODE
//    LEFT JOIN INVENTORY i ON p.PRODUCT_CODE = i.PRODUCT_CODE
//    WHERE (pp.SEARCH_DATA LIKE '%' || :query || '%')
//    ORDER BY p.PRODUCT_CODE
//    LIMIT :limit
//"""
//    )
//    suspend fun searchProductByQuery(
//        query: String?, limit: Int
//    ): List<ProductInfo>

    @Query("SELECT _id FROM PRODUCT_PACKS WHERE PRODUCT_CODE = :productCode")
    suspend fun getPackIdForProductCode(productCode: Long): Long?
}
