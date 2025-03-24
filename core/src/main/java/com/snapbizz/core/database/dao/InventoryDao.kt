package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.snapbizz.core.database.entities.Inventory

@Dao
interface InventoryDao : GenericDao<Inventory> {

    @Query("SELECT * FROM INVENTORY")
    fun getAll(): List<Inventory>

    @Query("SELECT * FROM INVENTORY WHERE PRODUCT_CODE = :productCode")
    fun getByProductCode(productCode: Long): Inventory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsync(inventory: Inventory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSync(inventory: Inventory): Long

    @Update
    fun update(inventory: Inventory)

    @Query("DELETE FROM INVENTORY WHERE PRODUCT_CODE = :productCode")
    fun deleteByProductCode(productCode: Long)

    @Query("DELETE FROM INVENTORY")
    fun deleteAll()

    @Query("UPDATE INVENTORY SET QUANTITY = QUANTITY - :quantity , IS_SYNC_PENDING = 1 WHERE PRODUCT_CODE = :productCode")
    fun decreaseQuantityByProductCode(productCode: Long, quantity: Int)

//    @Query("""
//        SELECT * FROM (
//            SELECT
//                PRODUCT_PACKS.PRODUCT_CODE AS product_code,
//                PRODUCTS.NAME as name,
//                PRODUCTS.UOM AS uom,
//                INVENTORY.QUANTITY AS in_stock_qty,
//                IFNULL(INVENTORY.MINIMUM_BASE_QUANTITY, 0) AS mbq,
//                IFNULL(INVENTORY.RE_ORDER_POINT, 0) AS rop,
//                PRODUCTS.VAT_RATE AS vat_rate,
//                PRODUCTS.SGST_RATE AS sgst_rate,
//                PRODUCTS.CGST_RATE AS cgst_rate,
//                PRODUCTS.IGST_RATE AS igst_rate,
//                PRODUCTS.CESS_RATE AS cess_rate,
//                PRODUCTS.ADDITIONAL_CESS_RATE AS additional_cess_rate,
//                IFNULL(PRODUCT_PACKS.PURCHASE_PRICE, 0) AS pp,
//                IFNULL(PRODUCT_PACKS.SALE_PRICE1, 0) AS sp1,
//                IFNULL(PRODUCTS.MRP, 0) AS mrp,
//                CAST(IFNULL(PRODUCTS.HSN_CODE, " ") AS TEXT) AS hsn
//            FROM
//                PRODUCT_PACKS
//            LEFT JOIN
//                INVENTORY ON INVENTORY.PRODUCT_CODE = PRODUCT_PACKS.PRODUCT_CODE
//            LEFT JOIN
//                PRODUCTS ON PRODUCT_PACKS.PRODUCT_CODE = PRODUCTS.PRODUCT_CODE
//        ) WHERE
//            product_code IS NOT NULL AND UOM IS NOT NULL
//    """)
//    fun getStockReport(): List<StockReport>

    @Query("SELECT QUANTITY FROM INVENTORY WHERE PRODUCT_CODE = :productCode")
    fun getProductExistingQuantity(productCode:Long):Long
}