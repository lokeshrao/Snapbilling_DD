package com.snapbizz.onboarding.downSync

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.snapbizz.common.config.models.SyncApiService
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.helpers.LogModule
import com.snapbizz.core.helpers.LogPriority
import com.snapbizz.core.helpers.SnapLogger
import com.snapbizz.core.utils.DownSyncConfig
import com.snapbizz.core.database.dao.GenericDao
import com.snapbizz.core.utils.SnapPreferences
import com.snapbizz.onboarding.downSync.downloadSyncDto.InvoiceDto
import com.snapbizz.onboarding.downSync.downloadSyncDto.getAppointmentServicesSyncConfig
import com.snapbizz.onboarding.downSync.downloadSyncDto.getAppointmentsSyncConfig
import com.snapbizz.onboarding.downSync.downloadSyncDto.getCustomerDetailsSyncConfig
import com.snapbizz.onboarding.downSync.downloadSyncDto.getCustomersSyncConfig
import com.snapbizz.onboarding.downSync.downloadSyncDto.getDoctorsSyncConfig
import com.snapbizz.onboarding.downSync.downloadSyncDto.getInventorySyncConfig
import com.snapbizz.onboarding.downSync.downloadSyncDto.getInvoiceSyncConfig
import com.snapbizz.onboarding.downSync.downloadSyncDto.getProductCustomizationSyncConfig
import com.snapbizz.onboarding.downSync.downloadSyncDto.getProductPackSyncConfig
import com.snapbizz.onboarding.downSync.downloadSyncDto.getProductsSyncConfig
import com.snapbizz.onboarding.downSync.downloadSyncDto.getRepresentativeSyncConfig
import com.snapbizz.onboarding.downSync.downloadSyncDto.getTransactionSyncConfig
import com.snapbizz.onboarding.downSync.downloadSyncDto.invoiceDtoToEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class DownSyncHelper @Inject constructor(
    private val syncApiService: SyncApiService,
    private val snapDatabase: SnapDatabase
) {
    private val tableConfigs = listOf(
        getInventorySyncConfig(snapDatabase),
        getProductPackSyncConfig(snapDatabase),
        getProductsSyncConfig(snapDatabase),
        getCustomersSyncConfig(snapDatabase),
        getCustomerDetailsSyncConfig(snapDatabase),
        getInvoiceSyncConfig(snapDatabase),
        getTransactionSyncConfig(snapDatabase),
        getProductCustomizationSyncConfig(snapDatabase),
        getAppointmentsSyncConfig(snapDatabase),
        getRepresentativeSyncConfig(snapDatabase),
        getDoctorsSyncConfig(snapDatabase)
    )

    suspend fun doDownloadSync(syncStatus: MutableStateFlow<String?>): Result<Unit> {
        return try {
            clearAllTables()
            for (config in tableConfigs) {
                syncStatus.update { "Syncing ${config.tableName}" }
                var offset: String? = "0"
                while (true) {
                    SnapLogger.log("Sync","Calling API for ${config.tableName} with offset: $offset", LogModule.HOME, LogPriority.HIGH)
                    val response =
                        syncApiService.getData(config.tableName,SnapPreferences.STORE_ID,offset.toString())?.body()
                    if (response != null) {
                        SnapLogger.log("Sync","Response for ${config.tableName}: $response", LogModule.HOME, LogPriority.HIGH)
                        val status = response["status"]?.asString
                        val offsetJsonElement = response["offset"]

                        when (status) {
                            "Not found" -> break
                            "Success" -> {
                                val dao = config.daoProvider(snapDatabase)
                                handleSuccessResponse(response, config, dao)
                                if (offsetJsonElement != null && !offsetJsonElement.isJsonNull) {
                                    offset = offsetJsonElement.asString
                                } else {
                                    SnapLogger.log("Sync","Offset not found or is null, breaking the loop.", LogModule.HOME, LogPriority.HIGH)
                                    break
                                }
                            }

                            else -> {
                                SnapLogger.log("Sync","Unexpected status: $status", LogModule.HOME, LogPriority.HIGH)
                                clearAllTables()
                                return Result.failure(Exception("Unexpected status: $status"))
                            }
                        }
                    } else {
                        SnapLogger.log("Sync","Error syncing ${config.tableName} table: Response is null", LogModule.HOME, LogPriority.HIGH)
                        clearAllTables()
                        return Result.failure(Exception("Error syncing ${config.tableName} table"))
                    }
                }
            }
            Result.success(Unit)
        } catch (ex: Exception) {
            SnapLogger.log("Sync","Error during download sync: ${ex.message}", LogModule.HOME, LogPriority.HIGH)
            clearAllTables()
            Result.failure(Exception("Error during download sync :: Reason ${ex.message}"))
        }
    }

    private suspend fun <T : Any, D : Any> handleSuccessResponse(
        response: JsonObject,
        config: DownSyncConfig<T, D>,
        dao: GenericDao<Any>,
    ) {
        try {
            if (config.tableName == "invoices") {
                saveInvoiceDataToDao(response)
            } else {
                saveDataToDao(response, config, dao)
            }
        } catch (ex: Exception) {
            SnapLogger.log("Sync","Error saving data to DAO for ${config.tableName}: ${ex.message}", LogModule.HOME, LogPriority.HIGH)
            clearAllTables()
            throw Exception("Error saving data to DAO for ${config.tableName} :: Reason ${ex.message}")
        }
    }

    private suspend fun <T : Any, D : Any> saveDataToDao(
        response: JsonObject, config: DownSyncConfig<T, D>, dao: GenericDao<Any>
    ) {
        try {
            val jsonArray = response.getAsJsonArray(config.jsonKey)
            val dtoType = TypeToken.getParameterized(List::class.java, config.dtoClass).type
            val apiDataList: List<D> = Gson().fromJson(jsonArray, dtoType)

            config.dtoToEntityMapper?.let { mapper ->
                val dbDataList = apiDataList.map(mapper)
                dao.insertOrUpdate(dbDataList)
            }
        } catch (ex: Exception) {
            SnapLogger.log("Sync","Error saving data to DAO for ${config.tableName}: ${ex.message}", LogModule.HOME, LogPriority.HIGH)
            throw ex
        }
    }

    private suspend fun saveInvoiceDataToDao(response: JsonObject) {
        try {
            val jsonArray = response.getAsJsonArray("invoicesList")
            val dtoType = TypeToken.getParameterized(List::class.java, InvoiceDto::class.java).type
            val apiDataList: List<InvoiceDto> = Gson().fromJson(jsonArray, dtoType)

            apiDataList.forEach { apiInvoice ->
                val (invoice, items) = invoiceDtoToEntity(apiInvoice)
                snapDatabase.invoiceDao().insert(invoice)
                if (items.isNotEmpty()) {
                    snapDatabase.itemsDao().insertOrUpdate(items)
                }
            }
        } catch (ex: Exception) {
            SnapLogger.log("Sync","Error saving invoice data to DAO: ${ex.message}", LogModule.HOME, LogPriority.HIGH)
            throw ex
        }
    }

    private fun clearAllTables() {
        try {
            snapDatabase.runInTransaction {
                snapDatabase.productsDao().deleteAll()
                snapDatabase.productPacksDao().deleteAll()
                snapDatabase.inventoryDao().deleteAll()
                snapDatabase.customerDao().deleteAll()
                snapDatabase.customerDetailsDao().deleteAll()
                snapDatabase.invoiceDao().deleteAll()
                snapDatabase.transactionsDao().deleteAll()
            }
        } catch (ex: Exception) {
            SnapLogger.log("Sync","Error clearing all tables: ${ex.message}", LogModule.HOME, LogPriority.HIGH)
        }
    }
}
