package com.snapbizz.billing

import com.snapbizz.billing.apiService.PaymentApiService
import com.snapbizz.billing.QuickPayRepository
import com.snapbizz.common.models.AppKeysData
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.utils.SnapPreferences
import javax.inject.Inject

class QuickPayRepositoryImpl @Inject constructor(
    private val snapDatabase: SnapDatabase,
    private val paymentApiService: PaymentApiService
): QuickPayRepository {
    override fun getAppKeys(): Result<AppKeysData> {
        return try {
            val response = paymentApiService.getAppKeys(
                SnapPreferences.STORE_ID.toString(),
                SnapPreferences.DEVICE_ID,
                SnapPreferences.ACCESS_TOKEN,
                SnapPreferences.v4Token.toString()).execute()
            val result = response.body()
            if (response.isSuccessful && result?.status.equals("Success",true)) {
                Result.success(result?.appKeysData?: AppKeysData())
            } else {
                Result.failure(Exception(result?.message))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(Exception("Something went wrong"))
        }
    }

}