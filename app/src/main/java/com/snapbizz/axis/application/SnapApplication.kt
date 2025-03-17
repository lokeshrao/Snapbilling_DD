package com.snapbizz.axis.application

import android.app.Application
import com.snapbizz.core.database.dao.LogDao
import com.snapbizz.core.helpers.ConfigManager
import com.snapbizz.core.helpers.FirebaseManager
import com.snapbizz.core.helpers.SnapLogger
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class SnapApplication : Application(){
    @Inject
    lateinit var logsDao:LogDao

    override fun onCreate() {
        super.onCreate()
        FirebaseManager.initFirebase(this)
        CoroutineScope(Dispatchers.IO).launch {
            ConfigManager(this@SnapApplication).loadConfigOnInit()
        }
        SnapLogger.init(this,logsDao)

    }
}
