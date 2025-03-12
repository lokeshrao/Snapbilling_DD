package com.snapbizz.axis.application

import android.app.Application
import com.snapbizz.core.helpers.ConfigManager
import com.snapbizz.core.helpers.FirebaseManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class SnapApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        FirebaseManager.initFirebase(this)
        CoroutineScope(Dispatchers.IO).launch {
            ConfigManager(this@SnapApplication).loadConfigOnInit()
        }
    }
}
