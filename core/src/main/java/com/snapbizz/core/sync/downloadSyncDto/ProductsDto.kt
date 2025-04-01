package com.snapbizz.core.sync.downloadSyncDto

import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.entities.Products
import com.snapbizz.core.utils.DownSyncConfig
import java.util.Date

data class ProductsDto(
    @SerializedName("product_code")
    val productCode: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("mrp")
    val mrp: Long,

    @SerializedName("uom")
    val uom: String,

    @SerializedName("image")
    val image: String?,

    @SerializedName("vat_rate")
    val vatRate: Double,

    @SerializedName("is_gdb")
    val isGdb: Boolean,

    @SerializedName("category_id")
    val categoryId: Int?,

    @SerializedName("is_pc")
    val isPc: Boolean,

    @SerializedName("is_deleted")
    val isDeleted: Boolean,

    @SerializedName("created_at")
    val createdAt: Date,

    @SerializedName("updated_at")
    val updatedAt: Date,

    @SerializedName("hsn_code")
    val hsnCode: String?,

    @SerializedName("cgst_rate")
    val cgstRate: Double?,

    @SerializedName("sgst_rate")
    val sgstRate: Double?,

    @SerializedName("igst_rate")
    val igstRate: Double?,

    @SerializedName("cess_rate")
    val cessRate: Double?,

    @SerializedName("additional_cess_rate")
    val additionalCessRate: Double?,

    @SerializedName("barcode")
    val barcode: Long,

    @SerializedName("is_snaporder_sync")
    val isSnapOrderSync: Boolean,

    @SerializedName("is_exclusive_gst")
    val isExclusiveGst: Boolean
)

fun productDtoToEntity(apiProduct: ProductsDto): Products {
    return Products(
        productCode = apiProduct.productCode,
        name = apiProduct.name,
        mrp = apiProduct.mrp,
        uom = apiProduct.uom,
        vatRate = apiProduct.vatRate,
        categoryId = apiProduct.categoryId,
        image = apiProduct.image,
        isGdb = apiProduct.isGdb,
        isDeleted = apiProduct.isDeleted,
        createdAt = apiProduct.createdAt,
        updatedAt = apiProduct.updatedAt,
        hsnCode = apiProduct.hsnCode,
        sgstRate = apiProduct.sgstRate,
        cgstRate = apiProduct.cgstRate,
        igstRate = apiProduct.igstRate,
        cessRate = apiProduct.cessRate,
        additionalCessRate = apiProduct.additionalCessRate,
        barcode = apiProduct.barcode,
        isSnapOrderSync = apiProduct.isSnapOrderSync,
        isSyncPending = false,
        isExclusiveGst = apiProduct.isExclusiveGst
    )
}

fun getProductsSyncConfig(snapDb: SnapDatabase): DownSyncConfig<Products, ProductsDto> {
    return DownSyncConfig(
        tableName = "products",
        jsonKey = "productsList",
        entityClass = Products::class.java,
        dtoClass = ProductsDto::class.java,
        daoProvider = { snapDb.productsDao() },
        dtoToEntityMapper = ::productDtoToEntity
    )
}

fun productToEntity(product: Products): ProductsDto {
    return ProductsDto(
        productCode = product.productCode,
        name = product.name,
        mrp = product.mrp,
        uom = product.uom,
        vatRate = product.vatRate?:0.0,
        categoryId = product.categoryId,
        image = product.image,
        isPc = false,
        isGdb = product.isGdb,
        isDeleted = product.isDeleted,
        createdAt = product.createdAt,
        updatedAt = product.updatedAt,
        hsnCode = product.hsnCode,
        sgstRate = product.sgstRate,
        cgstRate = product.cgstRate,
        igstRate = product.igstRate,
        cessRate = product.cessRate,
        additionalCessRate = product.additionalCessRate,
        barcode = product.barcode,
        isSnapOrderSync = product.isSnapOrderSync ?: false,
        isExclusiveGst = product.isExclusiveGst ?: false
    )
}

fun convertProductListToUpSyncDtoList(products: List<Products>): List<ProductsDto> {
    return products.map { product -> productToEntity(product) }
}

