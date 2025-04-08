package com.snapbizz.axis.home

import com.snapbizz.common.models.AppKeysData
import com.snapbizz.common.models.GenerateTokenRequestBody
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.core.helpers.LogModule
import com.snapbizz.core.helpers.SnapLogger
import com.snapbizz.core.network.HomeApiService
import com.snapbizz.core.utils.SnapPreferences
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val snapDatabase: SnapDatabase,
    private val snapDataStore: SnapDataStore,
    private val apiService: HomeApiService
): HomeRepository {

    override suspend fun validateAndGetAppKeys(): Result<String> {
        return try {
            if (validateAppKeys()) {
                val response = apiService.getAppKeys(
                    SnapPreferences.STORE_ID.toString(),
                    SnapPreferences.DEVICE_ID,
                    SnapPreferences.ACCESS_TOKEN).execute()
                val result = response.body()
                if (response.isSuccessful && result?.status.equals("Success",true)) {
                    snapDataStore.setAppKeys(result?.appKeysData ?:AppKeysData())
                    Result.success("")
                } else {
                    Result.failure(Exception(result?.message))
                }
            } else {
                Result.failure(Exception("Invalid App Keys"))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(Exception("Something went wrong"))
        }
    }
    suspend fun validateAppKeys(): Boolean {
        return snapDataStore.getAppKeys().firstOrNull()?.run {
            listOf(appKey, mid, tid, userName, merchantName).all { it.isNullOrBlank() }
        } == true
    }

}