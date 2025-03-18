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

class ConfigManager(val snapDataStore: SnapDataStore) {

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    private val gson = Gson()

    suspend fun fetchConfig(source: Source) {
        Log.d("FIREBASE", "Source:$source")

        val json = when (source) {
            Source.FIREBASE -> fetchFromFirebase()
            is Source.API -> fetchFromAPI(source.url)
        }
        Log.d("FIREBASE", json.toString())
        json?.let {
            saveConfigToDataStore(it)
        }
    }

    private suspend fun fetchFromFirebase(): String = withContext(Dispatchers.IO) {
        Log.d("FIREBASE", "Fetching firebase logs")
        remoteConfig.fetchAndActivate().await()
        return@withContext remoteConfig.getString("app_config")
    }

    private suspend fun fetchFromAPI(url: String): String? = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        return@withContext client.newCall(request).execute().body?.string()
    }

    private suspend fun saveConfigToDataStore(json: String) {
        snapDataStore.saveConfig(json)
        val parsedConfig = gson.fromJson(json, ConfigResponse::class.java)
        SnapThemeConfig.update(parsedConfig)
    }

    fun getStoredConfig(): Flow<String?> {
        return snapDataStore.getConfigFlow()
    }

    suspend fun loadConfigOnInit() {
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
}

object FirebaseManager {
    fun initFirebase(context: Context) {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
    }
}





