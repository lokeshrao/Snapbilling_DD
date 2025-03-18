package com.snapbizz.axis.application

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.snapbizz.core.database.dao.LogDao
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.core.helpers.ConfigManager
import com.snapbizz.core.helpers.FirebaseManager
import com.snapbizz.core.helpers.SnapLogger
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
    @Inject
    lateinit var snapDataStore: SnapDataStore

    override fun onCreate() {
        super.onCreate()
        FirebaseManager.initFirebase(this)
        CoroutineScope(Dispatchers.IO).launch {
            ConfigManager(snapDataStore).loadConfigOnInit()
        }
        SnapLogger.init(this,logsDao)
        getRemoteConfig(snapDataStore)

    }
}
fun getRemoteConfig(snapDataStore: SnapDataStore) {
    val configManager = ConfigManager(snapDataStore)
    CoroutineScope(Job() + Dispatchers.IO).launch {
        configManager.fetchConfig(ConfigManager.Source.FIREBASE)
    }
}
