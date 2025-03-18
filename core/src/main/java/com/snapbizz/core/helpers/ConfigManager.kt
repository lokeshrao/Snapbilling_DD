package com.snapbizz.core.helpers

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.gson.Gson
import com.snapbizz.common.config.ConfigResponse
import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.core.datastore.SnapDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber

suspend fun fetchAndApplyConfig(snapDataStore: SnapDataStore, source: Source) {
    val remoteConfig = Firebase.remoteConfig
    val gson = Gson()

    val json = when (source) {
        Source.FIREBASE -> {
            remoteConfig.fetchAndActivate().await()
            remoteConfig.getString("app_config")
        }
        is Source.API -> {
            val client = OkHttpClient()
            val request = Request.Builder().url(source.url).build()
            client.newCall(request).execute().body?.string()
        }
    }

    json?.let {
        snapDataStore.saveConfig(it)
        val parsedConfig = gson.fromJson(it, ConfigResponse::class.java)
        SnapThemeConfig.update(parsedConfig)
    }
}
suspend fun loadConfigOnInit(snapDataStore: SnapDataStore) {
    val gson = Gson()
    val storedJson = snapDataStore.getConfigFlow().first()
    if(storedJson?.isNotEmpty() == true) {
        val config = gson.fromJson(storedJson, ConfigResponse::class.java)
        SnapThemeConfig.update(config)
    }
}

sealed class Source {
    data object FIREBASE : Source()
    data class API(val url: String) : Source()
}
object FirebaseManager {
    fun initFirebase(context: Context) {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
    }
}





