package com.snapbizz.core.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.snapbizz.core.helpers.LogModule
import com.snapbizz.core.helpers.SnapLogger
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class UploadSyncer @AssistedInject constructor(
    private val syncRepository: SyncRepository,
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val totalSteps = 3 // Number of tables being synced
            var currentStep = 0
            SnapLogger.log("WORKER", "doWork: work done", LogModule.HOME)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

fun StartSyncWorker(context: Context){
    val periodicWorkRequest = PeriodicWorkRequestBuilder<UploadSyncer>(15, TimeUnit.MINUTES)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "SnapSyncWorker",
        ExistingPeriodicWorkPolicy.KEEP,
        periodicWorkRequest
    )
}
