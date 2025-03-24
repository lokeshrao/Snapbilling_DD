package com.snapbizz.axis.application

import android.app.Application
import com.snapbizz.core.database.SnapGlobalDatabase
import com.snapbizz.core.database.dao.LogDao
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.core.datastore.SnapDataStoreEntryPoint
import com.snapbizz.core.helpers.FirebaseManager
import com.snapbizz.core.helpers.SnapLogger
import com.snapbizz.core.helpers.Source
import com.snapbizz.core.helpers.fetchAndApplyConfig
import com.snapbizz.core.helpers.loadConfigOnInit
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class SnapApplication : Application(){
    @Inject
    lateinit var logsDao:LogDao

    override fun onCreate() {
        super.onCreate()
        FirebaseManager.initFirebase(this)
        CoroutineScope(Job()+ Dispatchers.IO).launch {
            var i = SnapGlobalDatabase.getDatabase(this@SnapApplication)
            var ij = i.gstDao().getGstById(1)
        }
        CoroutineScope(Dispatchers.IO).launch {
            CoroutineScope(Dispatchers.IO).launch {
                val snapDataStore :SnapDataStore = EntryPointAccessors.fromApplication(this@SnapApplication, SnapDataStoreEntryPoint::class.java).snapDataStore
                snapDataStore.let {
                    loadConfigOnInit(it)
                    getRemoteConfig(it)
                }
            }
        }
        SnapLogger.init(this,logsDao)
    }
}
fun getRemoteConfig(snapDataStore: SnapDataStore) {
    CoroutineScope(Job() + Dispatchers.IO).launch {
        fetchAndApplyConfig(snapDataStore,Source.FIREBASE)
    }
}
