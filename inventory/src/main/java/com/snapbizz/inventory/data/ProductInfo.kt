package com.snapbizz.inventory.data
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.Date

data class ProductInfo(
    var productCode: Long?,
    var name: String?,
    var mrp: Long?,
    var uom: String?,
    val vatRate: Double?,
    val categoryId: Int?,
    var categoryName: String?,
    var parentCategoryName: String?,
    val image: String?,
    val isGdb: Boolean?,
    val isDeleted: Boolean?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val hsnCode: String?,
    val sgstRate: Double?,
    val cgstRate: Double?,
    val igstRate: Double?,
    val cessRate: Double?,
    val additionalCessRate: Double?,
    val barcode: Long?,
    val isSnapOrderSync: Boolean?,
    val isSyncPending: Boolean?,
    val isExclusiveGst: Boolean?,
    val packSize: Int?,
    val salePrice1: Long?,
    val salePrice2: Long?,
    val salePrice3: Long?,
    var purchasePrice: Long?,
    val isDefault: Boolean?,
    val packBarcode: Long?,
    val expDate: Date?,
    var inventoryQuantity: Long?,
    val minimumBaseQuantity: Long?,
    val reOrderPoint: Long?,
    val isQuickAdd: Boolean?,
    val farmerShare: Long?
)


data class InventoryInfo(
    var productCode: MutableState<String?> = mutableStateOf(""),
    var quantity: MutableState<String?> = mutableStateOf(""),
    var minimumBaseQuantity: MutableState<String?> = mutableStateOf(""),
    var reOrderPoint: MutableState<String> = mutableStateOf(""),
    var isDeleted: MutableState<String> = mutableStateOf(""),
    var createdAt: MutableState<String> = mutableStateOf(""),
    var updatedAt: MutableState<String> = mutableStateOf(""),
    var isSyncPending: MutableState<String> = mutableStateOf("")
)