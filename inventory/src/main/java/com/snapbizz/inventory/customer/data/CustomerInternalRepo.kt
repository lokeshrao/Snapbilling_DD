package com.snapbizz.inventory.customer.data

import androidx.paging.PagingData
import com.snapbizz.common.models.CustomerInfo
import kotlinx.coroutines.flow.Flow

interface CustomerInternalRepo {
    fun addNewCustomer(value: CustomerInfo?): Boolean
    fun getCustomerByPhone(query: Long?): Flow<PagingData<CustomerInfo>>
}
