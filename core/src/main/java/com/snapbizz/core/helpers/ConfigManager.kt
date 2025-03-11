package com.snapbizz.core.helpers

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import com.google.gson.annotations.SerializedName
import com.snapbizz.core.datastore.SnapDataStore
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.Request

class ConfigManager(private val context: Context) {

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    private val _configFlow = MutableStateFlow<ConfigResponse?>(null)
    val configFlow: Flow<ConfigResponse?> = _configFlow.asStateFlow()
    private val gson = Gson()

    suspend fun fetchConfig(source: Source) {
        Log.d("FIREBASE","Source:$source")

        val json = when (source) {
            Source.FIREBASE -> fetchFromFirebase()
            is Source.API -> fetchFromAPI(source.url)
        }
        Log.d("FIREBASE",json.toString())
        json?.let {
            saveConfigToDataStore(it)
            _configFlow.value = gson.fromJson(it, ConfigResponse::class.java)
        }
    }

    private suspend fun fetchFromFirebase(): String = withContext(Dispatchers.IO) {
        Log.d("FIREBASE","Fetching firebase logs")
        remoteConfig.fetchAndActivate().await()
        return@withContext remoteConfig.getString("app_config")
    }

    private suspend fun fetchFromAPI(url: String): String? = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        return@withContext client.newCall(request).execute().body?.string()
    }

    private suspend fun saveConfigToDataStore(json: String) {
        SnapDataStore.saveConfig(context, json)
    }

    fun getStoredConfig(): Flow<String?> {
        return SnapDataStore.getConfigFlow(context)
    }

    sealed class Source {
        object FIREBASE : Source()
        data class API(val url: String) : Source()
    }
}


data class ConfigResponse(
    @SerializedName("theme") val theme: ThemeConfig,
    @SerializedName("features") val features: FeatureConfig
)

data class ThemeConfig(
    @SerializedName("primary_color") val primaryColor: String,
    @SerializedName("secondary_color") val secondaryColor: String,
    @SerializedName("tertiary_color") val tertiaryColor: String,
    @SerializedName("primary_bg_color") val primaryBgColor: String,
    @SerializedName("secondary_bg_color") val secondaryBgColor: String,
    @SerializedName("tertiary_bg_color") val tertiaryBgColor: String,
    @SerializedName("on_primary_color") val onPrimaryColor: String,
    @SerializedName("on_secondary_color") val onSecondaryColor: String,
    @SerializedName("on_tertiary_color") val onTertiaryColor: String,
    @SerializedName("text_color") val textColor: String,
    @SerializedName("hint_color") val hintColor: String,
    @SerializedName("text_size_title") val textSizeTitle: Int,
    @SerializedName("text_size_body") val textSizeBody: Int
)

data class FeatureConfig(
    @SerializedName("cart") val cart: Boolean,
    @SerializedName("reports") val reports: Boolean,
    @SerializedName("push_notifications") val pushNotifications: Boolean,
    @SerializedName("analytics") val analytics: Boolean,
    @SerializedName("dark_mode") val darkMode: Boolean,
    @SerializedName("voice_search") val voiceSearch: Boolean
)


object FirebaseManager {
    fun initFirebase(context: Context) {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
    }
}
