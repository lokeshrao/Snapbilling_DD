package com.snapbizz.core.sync.downloadSyncDto

import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.entities.Customer
import com.snapbizz.core.utils.DownSyncConfig
import java.util.Date

data class CustomersDto(
    @SerializedName("phone") val phone: Long? = null,

    @SerializedName("name") val name: String? = null,

    @SerializedName("address") val address: String? = null,

    @SerializedName("email") val email: String? = null,

    @SerializedName("credit_limit") val creditLimit: Long? = null,

    @SerializedName("is_disabled") val isDisabled: Boolean? = null,

    @SerializedName("created_at") val createdAt: Date? = null,

    @SerializedName("updated_at") val updatedAt: Date? = null,

    @SerializedName("gstin") val gstin: String? = null,

    @SerializedName("alt_phone") val altPhone: Long? = null,

    @SerializedName("is_alt_phone_selected") val isAltPhoneSelected: Boolean? = null
)

fun customerDtoToEntity(apiCustomer: CustomersDto): Customer {
    return Customer(
        phone = apiCustomer.phone ?: 0,
        name = apiCustomer.name ?: "",
        address = apiCustomer.address,
        email = apiCustomer.email,
        creditLimit = apiCustomer.creditLimit ?: 0,
        isDisabled = apiCustomer.isDisabled ?: false,
        isSyncPending = false,
        isSnapOrderSync = false,
        createdAt = apiCustomer.createdAt ?: Date(),
        updatedAt = apiCustomer.updatedAt ?: Date(),
        gstin = apiCustomer.gstin,
        altPhone = apiCustomer.altPhone,
        isAltPhoneSelected = apiCustomer.isAltPhoneSelected ?: false
    )
}

fun getCustomersSyncConfig(snapDb: SnapDatabase): DownSyncConfig<Customer, CustomersDto> {
    return DownSyncConfig(
        tableName = "customers",
        jsonKey = "customersList",
        entityClass = Customer::class.java,
        dtoClass = CustomersDto::class.java,
        daoProvider = { snapDb.customerDao() },
        dtoToEntityMapper = ::customerDtoToEntity
    )
}

fun customerEntityToDto(customer: Customer): CustomersDto {
    return CustomersDto(
        address = customer.address,
        name = customer.name,
        phone = customer.phone,
        email = customer.email,
        creditLimit = customer.creditLimit,
        isDisabled = customer.isDisabled,
        createdAt = customer.createdAt,
        updatedAt = customer.updatedAt,
        gstin = customer.gstin,
        altPhone = customer.altPhone,
        isAltPhoneSelected = customer.isAltPhoneSelected,
    )
}

fun convertCustomerListToUpSyncDtoList(customers: List<Customer>): List<CustomersDto> {
    return customers.map { customer -> customerEntityToDto(customer) }
}

//suspend fun upSyncCustomer(
//    dao: CustomerDao, syncApiService: SyncApiService, onStart: (String) -> Unit
//) {
//    onStart("Syncing Customers")
//    syncData(
//        dao,
//        primaryKeyColumn = "PHONE",
//        syncStatusColumn = "IS_SYNC_PENDING",
//        tableName = "CUSTOMERS",
//        apiUrl = "v3/api/${Preferences.STORE_ID}/customers",
//        convertToApiObjectList = ::convertCustomerListToUpSyncDtoList,
//        syncApiService = syncApiService
//    )
//}
