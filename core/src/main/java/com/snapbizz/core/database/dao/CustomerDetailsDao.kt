package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.snapbizz.core.database.entities.CustomerDetails

@Dao
interface CustomerDetailsDao : GenericDao<CustomerDetails> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customerDetails: CustomerDetails)

    @Update
    suspend fun update(customerDetails: CustomerDetails)

    @Delete
    suspend fun delete(customerDetails: CustomerDetails)

    @Query("SELECT * FROM CUSTOMER_DETAILS WHERE phone = :phone")
    suspend fun getCustomerDetailsByPhone(phone: Long): CustomerDetails?

    @Query("SELECT * FROM CUSTOMER_DETAILS")
    suspend fun getAllCustomerDetails(): List<CustomerDetails>

    @Query("DELETE FROM CUSTOMER_DETAILS")
    fun deleteAll()
}
