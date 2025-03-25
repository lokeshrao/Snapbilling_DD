package com.snapbizz.inventory.screen

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.snapbizz.core.database.SnapGlobalDatabase
import com.snapbizz.core.database.entities.global.GlobalProduct
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GlobalDbRepository @Inject constructor(val snapGlobalDatabase: SnapGlobalDatabase) :
    IGlobalDbRepository {
    override fun getProducts(query: String): Flow<PagingData<GlobalProduct>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                snapGlobalDatabase.globalProductDao().getProducts(query)
            }).flow
    }
}

interface IGlobalDbRepository {
    fun getProducts(query: String): Flow<PagingData<GlobalProduct>>
}