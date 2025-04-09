package com.snapbizz.inventory.customer.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.snapbizz.common.config.CustomerRepository
import com.snapbizz.common.models.CustomerInfo
import com.snapbizz.core.database.SnapDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val snapDatabase: SnapDatabase
) : CustomerInternalRepo, CustomerRepository {

    override fun addNewCustomer(value: CustomerInfo?): Boolean {
        return snapDatabase.runInTransaction<Boolean> {
            val customer = convertToCustomer(value)?.let {
                snapDatabase.customerDao().insertOrUpdateSync(it)
            } ?: -1L

            return@runInTransaction customer > 0
        }
    }

    override fun getCustomerByPhone(query: Long?): Flow<PagingData<CustomerInfo>> {
        TODO("Not yet implemented")
    }


}