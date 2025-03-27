package com.snapbizz.core.sync.downloadSyncDto

import com.google.gson.annotations.SerializedName
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.entities.CustomerDetails
import com.snapbizz.core.utils.DownSyncConfig
import java.util.Date

data class CustomerDetailsDto(
    @SerializedName("phone") val phone: Long?,
    @SerializedName("amount_due") val amountDue: Long?,
    @SerializedName("amount_saved") val amountSaved: Long?,
    @SerializedName("purchase_value") val purchaseValue: Long?,
    @SerializedName("last_purchase_date") val lastPurchaseDate: Date?,
    @SerializedName("last_payment_date") val lastPaymentDate: Date?,
    @SerializedName("last_purchase_amount") val lastPurchaseAmount: Long?,
    @SerializedName("last_payment_amount") val lastPaymentAmount: Long?,
    @SerializedName("total_visits") val totalVisits: Int?,
    @SerializedName("created_at") val createdAt: Date?,
    @SerializedName("updated_at") val updatedAt: Date?,
    @SerializedName("reward_points") val rewardPoints: Int?
)


fun customerDetailsDtoToEntity(apiCustomerDetails: CustomerDetailsDto): CustomerDetails {
    return CustomerDetails(
        phone = apiCustomerDetails.phone ?: 0L,
        amountDue = apiCustomerDetails.amountDue ?: 0L,
        purchaseValue = apiCustomerDetails.purchaseValue ?: 0L,
        amountSaved = apiCustomerDetails.amountSaved ?: 0L,
        lastPurchaseAmount = apiCustomerDetails.lastPurchaseAmount ?: 0L,
        lastPaymentAmount = apiCustomerDetails.lastPaymentAmount ?: 0L,
        totalVisits = apiCustomerDetails.totalVisits ?: 0,
        lastPurchaseDate = apiCustomerDetails.lastPurchaseDate ?: Date(),
        lastPaymentDate = apiCustomerDetails.lastPaymentDate ?: Date(),
        createdAt = apiCustomerDetails.createdAt ?: Date(),
        updatedAt = apiCustomerDetails.updatedAt ?: Date(),
        rewardPoints = apiCustomerDetails.rewardPoints ?: 0,
        isSyncPending = false
    )
}

fun customerDetailsToUpSyncDto(customerDetails: CustomerDetails): CustomerDetailsDto {
    return CustomerDetailsDto(
        phone = customerDetails.phone,
        amountDue = customerDetails.amountDue,
        amountSaved = customerDetails.amountSaved,
        purchaseValue = customerDetails.purchaseValue,
        lastPurchaseDate = customerDetails.lastPurchaseDate,
        lastPaymentDate = customerDetails.lastPaymentDate,
        lastPurchaseAmount = customerDetails.lastPurchaseAmount,
        lastPaymentAmount = customerDetails.lastPaymentAmount,
        totalVisits = customerDetails.totalVisits,
        createdAt = customerDetails.createdAt,
        updatedAt = customerDetails.updatedAt,
        rewardPoints = customerDetails.rewardPoints
    )
}

fun customerDetailsListToUpSyncDto(customerDetailsList: List<CustomerDetails>): List<CustomerDetailsDto> {
    return customerDetailsList.map { customerDetails -> customerDetailsToUpSyncDto(customerDetails) }
}

fun getCustomerDetailsSyncConfig(snapDb: SnapDatabase): DownSyncConfig<CustomerDetails, CustomerDetailsDto> {
    return DownSyncConfig(
        tableName = "customer_details",
        jsonKey = "customer_detailsList",
        entityClass = CustomerDetails::class.java,
        dtoClass = CustomerDetailsDto::class.java,
        daoProvider = { snapDb.customerDetailsDao() },
        dtoToEntityMapper = ::customerDetailsDtoToEntity
    )
}

//suspend fun upSyncCustomerDetails(
//    dao: CustomerDetailsDao, syncApiService: SyncApiService, onStart: (String) -> Unit
//) {
//    onStart("Syncing Customer Details")
//    syncData(
//        dao,
//        primaryKeyColumn = "PHONE",
//        syncStatusColumn = "IS_SYNC_PENDING",
//        tableName = "CUSTOMER_DETAILS",
//        apiUrl = "v3/api/${Preferences.STORE_ID}/customer_details",
//        convertToApiObjectList = ::customerDetailsListToUpSyncDto,
//        syncApiService = syncApiService
//    )
//}
