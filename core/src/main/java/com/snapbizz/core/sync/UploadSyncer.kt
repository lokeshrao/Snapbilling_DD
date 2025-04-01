package com.snapbizz.core.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.snapbizz.common.models.SyncApiService
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.convertInvoiceWithItemsToDto
import com.snapbizz.core.database.dao.GenericDao
import com.snapbizz.core.database.dao.Identifiable
import com.snapbizz.core.database.dao.InvoiceDao
import com.snapbizz.core.database.entities.Appointments
import com.snapbizz.core.database.entities.Customer
import com.snapbizz.core.database.entities.CustomerDetails
import com.snapbizz.core.database.entities.Doctors
import com.snapbizz.core.database.entities.Inventory
import com.snapbizz.core.database.entities.InvoiceWithItems
import com.snapbizz.core.database.entities.ProductCustomization
import com.snapbizz.core.database.entities.ProductPacks
import com.snapbizz.core.database.entities.Products
import com.snapbizz.core.database.entities.Representative
import com.snapbizz.core.database.entities.Transactions
import com.snapbizz.core.helpers.LogModule
import com.snapbizz.core.helpers.SnapLogger
import com.snapbizz.core.sync.downloadSyncDto.AppointmentsDto
import com.snapbizz.core.sync.downloadSyncDto.CustomerDetailsDto
import com.snapbizz.core.sync.downloadSyncDto.CustomersDto
import com.snapbizz.core.sync.downloadSyncDto.DoctorsDto
import com.snapbizz.core.sync.downloadSyncDto.InventoryDto
import com.snapbizz.core.sync.downloadSyncDto.InvoiceDto
import com.snapbizz.core.sync.downloadSyncDto.ProductCustomizationDto
import com.snapbizz.core.sync.downloadSyncDto.ProductPacksDto
import com.snapbizz.core.sync.downloadSyncDto.ProductsDto
import com.snapbizz.core.sync.downloadSyncDto.RepresentativeDto
import com.snapbizz.core.sync.downloadSyncDto.TransactionDto
import com.snapbizz.core.sync.downloadSyncDto.appointmentsToEntity
import com.snapbizz.core.sync.downloadSyncDto.customerDetailsToEntity
import com.snapbizz.core.sync.downloadSyncDto.customerEntityToDto
import com.snapbizz.core.sync.downloadSyncDto.doctorsToEntity
import com.snapbizz.core.sync.downloadSyncDto.inventoryToEntity
import com.snapbizz.core.sync.downloadSyncDto.productCustomizationToEntity
import com.snapbizz.core.sync.downloadSyncDto.productPacksToEntity
import com.snapbizz.core.sync.downloadSyncDto.productToEntity
import com.snapbizz.core.sync.downloadSyncDto.representativeToEntity
import com.snapbizz.core.sync.downloadSyncDto.transactionToEntity
import com.snapbizz.core.utils.DateTypeAdapter
import com.snapbizz.core.utils.SnapPreferences
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.collections.count
import kotlin.collections.isNotEmpty
import kotlin.collections.mapNotNull

interface BaseSyncer {
    suspend fun getPendingItem()
}

@HiltWorker
class UploadSyncer @AssistedInject constructor(
    private val syncRepository: SyncRepository,
    private val snapDatabase: SnapDatabase,
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    val module = LogModule.UPSYNC

    override suspend fun doWork(): Result {
        return try {
            syncRepository.snapDataStore.loadPrefs()
            val syncList = listOf(
                getCustomerSyncModel(snapDatabase),
                getInvoiceSync(snapDatabase),
                getCustomerDetailSyncModel(snapDatabase),
                inventorySyncModel(snapDatabase),
                productsSyncModel(snapDatabase),
                productPacksSyncModel(snapDatabase),
                appointmentsSyncModel(snapDatabase),
                doctorsSyncModel(snapDatabase),
                representativeSyncModel(snapDatabase),
                transactionSyncModel(snapDatabase),
                productCustomizationSyncModel(snapDatabase)
            )

            for (sync in syncList) {
                SnapLogger.log("Starting sync for", sync.tableName, module)
                try {
                    var offset = 0
                    do {
                        val query = buildQuery(sync.tableName, "IS_SYNC_PENDING", offset)
                        val pendingItems =if(sync.tableName=="INVOICES") (sync.daoProvider as InvoiceDao).getDataForSync(offset) else sync.daoProvider.getPendingItemsRaw(query) as List<Any>
                        SnapLogger.log(
                            "Item Pending For Sync",
                            "Items - ${pendingItems.count()}  Offset - $offset",
                            module
                        )
                        if (pendingItems.isNotEmpty()) {
                            val entityList = pendingItems.mapNotNull { item ->
                                sync.entityClass.cast(item)
                            }
                            val failedCount = syncChunks(
                                entityList,
                                sync.url,
                                sync.daoProvider,
                                createGsonWithDateAdapter(),
                                sync.entityToDtoMapper as (Identifiable) -> Any,
                                syncRepository.syncApiService,
                                sync.primaryKeyColumn,
                                sync.tableName
                            )
                            offset += failedCount
                        }

                    } while (pendingItems.isNotEmpty())
                    SnapLogger.log("WORKER", "doWork: work done", LogModule.HOME)
                    Timber.tag("SyncService").d("${sync.tableName} sync completed")
                } catch (e: Exception) {
                    Timber.tag("SyncService").e(e, "Sync failed for ${sync.tableName}")
                }
            }
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Upload sync failed")
            Result.retry()
        }
    }
}

private fun buildQuery(
    tableName: String, syncStatusColumn: String, offset: Int
): SimpleSQLiteQuery {
    return when (tableName) {
        "INVOICES" -> SimpleSQLiteQuery(
            """
            SELECT invoices.*, items.* 
            FROM invoices
            LEFT JOIN items ON invoices._id = items.invoice_id
            WHERE invoices.$syncStatusColumn = 1
            LIMIT 100 OFFSET $offset
            """
        )
        "APPOINTMENTS" -> SimpleSQLiteQuery(
            "SELECT appointments.*, appointment_services.* FROM appointments " +
                    "LEFT JOIN appointment_services ON appointments.APPOINTMENT_ID = appointment_services.APPOINTMENT_ID " +
                    "WHERE appointments.$syncStatusColumn = 1 LIMIT 100 OFFSET $offset")
        else -> SimpleSQLiteQuery(
            "SELECT * FROM $tableName WHERE $syncStatusColumn = 1 LIMIT 100 OFFSET $offset"
        )
    }
}


private suspend fun <T : Identifiable> syncChunks(
    items: List<T>,
    apiUrl: String,
    dao: GenericDao<*>,
    gson: Gson,
    convertToApiObjectList: (Identifiable) -> Any, // Ensure correct mapping
    syncApiService: SyncApiService,
    primaryKeyColumn: String,
    tableName: String
): Int {
    val maxRetries = 3
    val chunks = items.chunked(10)
    Timber.tag("SyncService").d("Total chunks to sync: ${chunks.size}")
    var offset = 0

    for (chunk in chunks) {
        var retryCount = 0
        var success = false
        while (retryCount < maxRetries && !success) {
            try {
                Timber.tag("SyncService")
                    .d("Processing chunk ${chunks.indexOf(chunk)} with ${chunk.size} items (Retry: $retryCount) and Offset $offset")

                val uploadDataObjects = chunk.map(convertToApiObjectList)

                val response =
                    syncApiService.uploadData(apiUrl, prepareDataForUpload(gson, uploadDataObjects))

                if (response?.status.equals("Success", true)) {
                    val markSyncDoneQuery = buildMarkSyncDoneQuery(
                        primaryKeyColumn, "IS_SYNC_PENDING", chunk, tableName
                    )
                    dao.markSyncSuccessRaw(markSyncDoneQuery)
                    Timber.tag("SyncService")
                        .d("Chunk sync successful for Chunk: ${chunks.indexOf(chunk)} with: ${chunk.size} items")
                    success = true
                } else {
                    Timber.tag("SyncService")
                        .w("Sync failed for chunk: ${chunks.indexOf(chunk)} of size ${chunk.size}")
                    retryCount++
                }
            } catch (e: Exception) {
                Timber.tag("SyncService")
                    .e(e, "Error syncing chunk: ${chunks.indexOf(chunk)} of size ${chunk.size}")
                retryCount++
            }
        }

        if (!success) {
            Timber.tag("SyncService")
                .w("Skipping chunk: ${chunks.indexOf(chunk)} after $maxRetries retries")
            offset += chunk.size
        }
    }
    Timber.tag("SyncService").d("All chunks processed with some failures. Failure Count = $offset")
    return offset
}

private fun <T : Identifiable> buildMarkSyncDoneQuery(
    primaryKeyColumn: String, syncStatusColumn: String, chunk: List<T>, tableName: String
): SimpleSQLiteQuery {
    val statusUpdate = " $syncStatusColumn = 0 "
    val ids = chunk.joinToString(",") { "'${it.getPrimaryKeyValue()}'" }
    return SimpleSQLiteQuery("UPDATE $tableName SET $statusUpdate WHERE $primaryKeyColumn IN ($ids)")
}

fun createGsonWithDateAdapter(): Gson {
    return GsonBuilder().registerTypeAdapter(Date::class.java, DateTypeAdapter()).create()
}

fun prepareDataForUpload(gson: Gson, dataList: List<Any>): JsonArray {
    return JsonArray().apply {
        dataList.forEach { add(gson.toJsonTree(it)) }
    }
}

fun Context.startSyncWorker() {
    val periodicWorkRequest = PeriodicWorkRequestBuilder<UploadSyncer>(15, TimeUnit.MINUTES).build()
    WorkManager.getInstance(this).enqueueUniquePeriodicWork(
        "SnapSyncWorker", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest
    )
}

data class SyncBase<E : Identifiable, D>(
    val tableName: String,
    val url: String,
    val entityClass: Class<E>,
    val dtoClass: Class<D>,
    val daoProvider: GenericDao<*>,
    val entityToDtoMapper: ((E) -> D)? = null,
    val primaryKeyColumn: String = "",
)

fun getCustomerSyncModel(snapDatabase: SnapDatabase) = SyncBase<Customer, CustomersDto>(
    tableName = "CUSTOMERS",
    url = "v3/api/${SnapPreferences.STORE_ID}/customers",
    entityClass = Customer::class.java,
    dtoClass = CustomersDto::class.java,
    daoProvider = snapDatabase.customerDao(),
    entityToDtoMapper = ::customerEntityToDto,
    primaryKeyColumn = "PHONE"
)
fun getInvoiceSync(snapDatabase: SnapDatabase) = SyncBase<InvoiceWithItems, InvoiceDto>(
    tableName = "INVOICES",
    url = "v4/api/${SnapPreferences.STORE_ID}/invoices_apos",
    entityClass = InvoiceWithItems::class.java,
    dtoClass = InvoiceDto::class.java,
    daoProvider = snapDatabase.invoiceDao(),
    entityToDtoMapper= ::convertInvoiceWithItemsToDto,
    primaryKeyColumn = "_id"
)
fun getCustomerDetailSyncModel(snapDatabase: SnapDatabase) = SyncBase<CustomerDetails, CustomerDetailsDto>(
    tableName = "CUSTOMER_DETAILS",
    url = "v3/api/${SnapPreferences.STORE_ID}/customer_details",
    entityClass = CustomerDetails::class.java,
    dtoClass = CustomerDetailsDto::class.java,
    daoProvider = snapDatabase.customerDetailsDao(),
    entityToDtoMapper = ::customerDetailsToEntity,
    primaryKeyColumn = "PHONE"
)
fun inventorySyncModel(snapDatabase: SnapDatabase) = SyncBase<Inventory, InventoryDto>(
    tableName = "INVENTORY",
    url = "v3/api/${SnapPreferences.STORE_ID}/inventory",
    entityClass = Inventory::class.java,
    dtoClass = InventoryDto::class.java,
    daoProvider = snapDatabase.inventoryDao(),
    entityToDtoMapper = ::inventoryToEntity,
    primaryKeyColumn = "PRODUCT_CODE"
)
fun productsSyncModel(snapDatabase: SnapDatabase) = SyncBase<Products, ProductsDto>(
    tableName = "PRODUCTS",
    url = "v3/api/${SnapPreferences.STORE_ID}/products",
    entityClass = Products::class.java,
    dtoClass = ProductsDto::class.java,
    daoProvider = snapDatabase.productsDao(),
    entityToDtoMapper = ::productToEntity,
    primaryKeyColumn = "PRODUCT_CODE"
)
fun productPacksSyncModel(snapDatabase: SnapDatabase) = SyncBase<ProductPacks, ProductPacksDto>(
    tableName = "PRODUCT_PACKS",
    url = "v3/api/${SnapPreferences.STORE_ID}/product_packs",
    entityClass = ProductPacks::class.java,
    dtoClass = ProductPacksDto::class.java,
    daoProvider = snapDatabase.productPacksDao(),
    entityToDtoMapper = ::productPacksToEntity,
    primaryKeyColumn = "_id"
)

fun appointmentsSyncModel(snapDatabase: SnapDatabase) = SyncBase<Appointments, AppointmentsDto>(
    tableName = "APPOINTMENTS",
    url = "v3/api/${SnapPreferences.STORE_ID}/appointments",
    entityClass = Appointments::class.java,
    dtoClass = AppointmentsDto::class.java,
    daoProvider = snapDatabase.appointmentsDao(),
    entityToDtoMapper = ::appointmentsToEntity,
    primaryKeyColumn = "APPOINTMENT_ID"
)

fun doctorsSyncModel(snapDatabase: SnapDatabase) = SyncBase<Doctors, DoctorsDto>(
    tableName = "DOCTORS",
    url = "v3/api/${SnapPreferences.STORE_ID}/doctors",
    entityClass = Doctors::class.java,
    dtoClass = DoctorsDto::class.java,
    daoProvider = snapDatabase.doctorsDao(),
    entityToDtoMapper = ::doctorsToEntity,
    primaryKeyColumn = "DOCTOR_ID"
)

fun representativeSyncModel(snapDatabase: SnapDatabase) = SyncBase<Representative, RepresentativeDto>(
    tableName = "REPRESENTATIVE",
    url = "v3/api/${SnapPreferences.STORE_ID}/representative",
    entityClass = Representative::class.java,
    dtoClass = RepresentativeDto::class.java,
    daoProvider = snapDatabase.representativeDao(),
    entityToDtoMapper = ::representativeToEntity,
    primaryKeyColumn = "REPRESENTATIVE_ID"
)

fun transactionSyncModel(snapDatabase: SnapDatabase) = SyncBase<Transactions, TransactionDto>(
    tableName = "TRANSACTIONS",
    url = "v4/api/${SnapPreferences.STORE_ID}/transactions_apos",
    entityClass = Transactions::class.java,
    dtoClass = TransactionDto::class.java,
    daoProvider = snapDatabase.transactionsDao(),
    entityToDtoMapper = ::transactionToEntity,
    primaryKeyColumn = "_id"
)

fun productCustomizationSyncModel(snapDatabase: SnapDatabase) = SyncBase<ProductCustomization, ProductCustomizationDto>(
    tableName = "PRODUCT_CUSTOMIZATION",
    url = "v3/api/${SnapPreferences.STORE_ID}/product_customizations",
    entityClass = ProductCustomization::class.java,
    dtoClass = ProductCustomizationDto::class.java,
    daoProvider = snapDatabase.productCustomizationDao(),
    entityToDtoMapper = ::productCustomizationToEntity,
    primaryKeyColumn = "PRODUCT_CODE"
)
