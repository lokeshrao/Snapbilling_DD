package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.snapbizz.core.database.entities.Customer
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao : GenericDao<Customer> {

    @Insert
    suspend fun insert(customer: Customer)

    @Update
    suspend fun update(customer: Customer)

    @Query("SELECT * FROM CUSTOMERS WHERE phone = :phone")
    fun getCustomerByPhone(phone: Long): Flow<Customer?>

    @Query("DELETE FROM CUSTOMERS WHERE phone = :phone")
    suspend fun deleteByPhone(phone: Long)

    @Query("DELETE FROM CUSTOMERS")
    fun deleteAll()

//    @Query(
//        """
//        SELECT
//            c.PHONE AS phone,
//            c.NAME AS name,
//            c.ADDRESS AS address,
//            c.EMAIL AS email,
//            c.CREDIT_LIMIT AS creditLimit,
//            c.IS_DISABLED AS isDisabled,
//            c.IS_SYNC_PENDING AS isSyncPending,
//            c.CREATED_AT AS createdAt,
//            c.UPDATED_AT AS updatedAt,
//            c.GSTIN AS gstin,
//            c.ALT_PHONE AS altPhone,
//            c.IS_ALT_PHONE_SELECTED AS isAltPhoneSelected,
//            c.IS_SNAP_ORDER_SYNC AS isSnapOrderSync,
//            cd.AMOUNT_DUE AS amountDue,
//            cd.PURCHASE_VALUE AS purchaseValue,
//            cd.AMOUNT_SAVED AS amountSaved,
//            cd.LAST_PURCHASE_AMOUNT AS lastPurchaseAmount,
//            cd.LAST_PAYMENT_AMOUNT AS lastPaymentAmount,
//            cd.TOTAL_VISITS AS totalVisits,
//            cd.LAST_PURCHASE_DATE AS lastPurchaseDate,
//            cd.LAST_PAYMENT_DATE AS lastPaymentDate,
//            cd.CREATED_AT AS detailsCreatedAt,
//            cd.UPDATED_AT AS detailsUpdatedAt,
//            cd.REWARD_POINTS AS rewardPoints,
//            COALESCE(cd.IS_SYNC_PENDING, 0) AS detailsIsSyncPending
//        FROM CUSTOMERS c
//        LEFT JOIN CUSTOMER_DETAILS cd ON c.PHONE = cd.PHONE
//        WHERE (:query IS NULL
//               OR c.NAME LIKE '%' || :query || '%'
//               OR c.PHONE LIKE '%' || :query || '%')
//        ORDER BY c.NAME
//        LIMIT :limit OFFSET :offset
//    """
//    )
//    suspend fun getCustomers(
//        query: String?, offset: Int?, limit: Int?
//    ): List<CustomerInfo>
//
//    @Query(
//        """
//        SELECT
//            COUNT(*) AS customerCount,
//            COALESCE(SUM(cd.AMOUNT_DUE), 0) AS totalAmountDue
//        FROM CUSTOMERS c
//        LEFT JOIN CUSTOMER_DETAILS cd ON c.PHONE = cd.PHONE
//        WHERE (:query IS NULL
//               OR c.NAME LIKE '%' || :query || '%'
//               OR c.PHONE LIKE '%' || :query || '%')
//    """
//    )
//    suspend fun getCustomersSummary(
//        query: String?
//    ): CustomerSummary?
//
//    @Query(
//        """
//        SELECT
//            c.PHONE AS phone,
//            c.NAME AS name,
//            c.ADDRESS AS address,
//            c.EMAIL AS email,
//            c.CREDIT_LIMIT AS creditLimit,
//            c.IS_DISABLED AS isDisabled,
//            c.IS_SYNC_PENDING AS isSyncPending,
//            c.CREATED_AT AS createdAt,
//            c.UPDATED_AT AS updatedAt,
//            c.GSTIN AS gstin,
//            c.ALT_PHONE AS altPhone,
//            c.IS_ALT_PHONE_SELECTED AS isAltPhoneSelected,
//            c.IS_SNAP_ORDER_SYNC AS isSnapOrderSync,
//            cd.AMOUNT_DUE AS amountDue,
//            cd.PURCHASE_VALUE AS purchaseValue,
//            cd.AMOUNT_SAVED AS amountSaved,
//            cd.LAST_PURCHASE_AMOUNT AS lastPurchaseAmount,
//            cd.LAST_PAYMENT_AMOUNT AS lastPaymentAmount,
//            cd.TOTAL_VISITS AS totalVisits,
//            cd.LAST_PURCHASE_DATE AS lastPurchaseDate,
//            cd.LAST_PAYMENT_DATE AS lastPaymentDate,
//            cd.CREATED_AT AS detailsCreatedAt,
//            cd.UPDATED_AT AS detailsUpdatedAt,
//            cd.REWARD_POINTS AS rewardPoints,
//            COALESCE(cd.IS_SYNC_PENDING, 0) AS detailsIsSyncPending
//        FROM CUSTOMERS c
//        LEFT JOIN CUSTOMER_DETAILS cd ON c.PHONE = cd.PHONE
//        WHERE (c.NAME LIKE '%' || :query || '%'
//               OR c.PHONE LIKE '%' || :query || '%')
//        ORDER BY c.NAME
//        LIMIT :limit
//        """
//    )
//    suspend fun getCustomerByNameOrPhone(query: String?, limit: Int?): List<CustomerInfo>?

}
