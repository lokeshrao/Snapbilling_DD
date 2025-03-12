package com.snapbizz.core.helpers

import android.content.Context
import android.graphics.Color.parseColor
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import com.google.gson.annotations.SerializedName
import com.snapbizz.core.datastore.SnapDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.Request
import androidx.compose.ui.graphics.Color
import android.graphics.Color.parseColor

class ConfigManager(private val context: Context) {

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
        SnapDataStore.saveConfig(context, json)
        val parsedConfig = gson.fromJson(json, ConfigResponse::class.java)
        AppConfig.update(parsedConfig)
    }

    fun getStoredConfig(): Flow<String?> {
        return SnapDataStore.getConfigFlow(context)
    }

    suspend fun loadConfigOnInit() {
        val storedJson = SnapDataStore.getConfigFlow(context).first()
        val config = gson.fromJson(storedJson, ConfigResponse::class.java)
        AppConfig.update(config)
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
) {
    companion object {
        fun default() = ThemeConfig(
            primaryColor = "#0A50A3",
            secondaryColor = "#000000",
            tertiaryColor = "#FF03DAC6",
            primaryBgColor = "#FFFFFF",
            secondaryBgColor = "#F5F5F5",
            tertiaryBgColor = "#E0E0E0",
            onPrimaryColor = "#FFFFFF",
            onSecondaryColor = "#FFFFFF",
            onTertiaryColor = "#000000",
            textColor = "#000000",
            hintColor = "#757575",
            textSizeTitle = 16,
            textSizeBody = 14
        )
    }
}

data class FeatureConfig(
    @SerializedName("cart") val cart: Boolean,
    @SerializedName("reports") val reports: Boolean,
    @SerializedName("push_notifications") val pushNotifications: Boolean,
    @SerializedName("analytics") val analytics: Boolean,
    @SerializedName("dark_mode") val darkMode: Boolean,
    @SerializedName("voice_search") val voiceSearch: Boolean
) {
    companion object {
        fun default() = FeatureConfig(
            cart = true,
            reports = false,
            pushNotifications = true,
            analytics = true,
            darkMode = false,
            voiceSearch = true
        )
    }
}


object FirebaseManager {
    fun initFirebase(context: Context) {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
    }
}


object AppConfig {
    var theme: ThemeConfig = ThemeConfig.default()
        private set
    var features: FeatureConfig = FeatureConfig.default()
        private set
    var Primary: Color = Color.Unspecified
        private set
    var Secondary: Color = Color.Unspecified
        private set
    var Tertiary: Color = Color.Unspecified
        private set

    var PrimaryBg: Color = Color.Unspecified
        private set
    var SecondaryBg: Color = Color.Unspecified
        private set
    var TertiaryBg: Color = Color.Unspecified
        private set

    var OnPrimary: Color = Color.Unspecified
        private set
    var OnSecondary: Color = Color.Unspecified
        private set
    var OnTertiary: Color = Color.Unspecified
        private set

    var Text: Color = Color.Unspecified
        private set
    var Hint: Color = Color.Unspecified
        private set

    fun update(config: ConfigResponse) {
        theme = config.theme
        features = config.features

        // Convert hex to Color objects
        Primary = getColor(config.theme.primaryColor)
        Secondary = getColor(config.theme.secondaryColor)
        Tertiary = getColor(config.theme.tertiaryColor)

        PrimaryBg = getColor(config.theme.primaryBgColor)
        SecondaryBg = getColor(config.theme.secondaryBgColor)
        TertiaryBg = getColor(config.theme.tertiaryBgColor)

        OnPrimary = getColor(config.theme.onPrimaryColor)
        OnSecondary = getColor(config.theme.onSecondaryColor)
        OnTertiary = getColor(config.theme.onTertiaryColor)

        Text = getColor(config.theme.textColor)
        Hint = getColor(config.theme.hintColor)

        Log.d("AppConfig", "Config updated: $config")
    }

    private fun getColor(colorHex: String): Color = Color(parseColor(colorHex))
}



