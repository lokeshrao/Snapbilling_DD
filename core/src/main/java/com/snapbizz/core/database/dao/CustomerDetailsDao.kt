package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.snapbizz.core.database.entities.CustomerDetails

@Dao
interface CustomerDetailsDao : GenericDao<CustomerDetails> {

    @Query("SELECT * FROM CUSTOMER_DETAILS WHERE phone = :phone")
    suspend fun getCustomerDetailsByPhone(phone: Long): CustomerDetails?

    @Query("SELECT * FROM CUSTOMER_DETAILS")
    suspend fun getAllCustomerDetails(): List<CustomerDetails>

    @Query("DELETE FROM CUSTOMER_DETAILS")
    fun deleteAll()
}
