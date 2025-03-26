package com.snapbizz.axis.application

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.snapbizz.core.database.dao.LogDao
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.core.datastore.di.SnapStoreProvider
import com.snapbizz.core.helpers.FirebaseManager
import com.snapbizz.core.helpers.SnapLogger
import com.snapbizz.core.helpers.Source
import com.snapbizz.core.helpers.fetchAndApplyConfig
import com.snapbizz.core.helpers.loadConfigOnInit
import com.snapbizz.core.sync.StartSyncWorker
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class SnapApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()


    @Inject
    lateinit var logsDao: LogDao


    override fun onCreate() {
        super.onCreate()
        FirebaseManager.initFirebase(this)
        CoroutineScope(Dispatchers.IO).launch {
            val snapDataStore: SnapDataStore = EntryPointAccessors.fromApplication(
                this@SnapApplication, SnapStoreProvider::class.java
            ).snapDataStore
            snapDataStore.let {
                loadConfigOnInit(it)
                getRemoteConfig(it)
            }
        }

        SnapLogger.init(this, logsDao)
        StartSyncWorker(this)

    }

}

fun getRemoteConfig(snapDataStore: SnapDataStore) {
    CoroutineScope(Job() + Dispatchers.IO).launch {
        fetchAndApplyConfig(snapDataStore, Source.FIREBASE)
    }
}
