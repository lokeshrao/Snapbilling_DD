package com.snapbizz.common.config

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils

object SnapThemeConfig {
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

    var ContainerBg: Color = Color.Unspecified
        private set
    var SurfaceBg: Color = Color.Unspecified
        private set

    var Success: Color = Color.Unspecified
        private set
    var Error: Color = Color.Unspecified
        private set

    fun update(config: ConfigResponse) {
        theme = config.theme
        features = config.features

        Primary = getColor(config.theme.primary)
        Secondary = getColor(config.theme.secondary)
        Tertiary = getColor(config.theme.tertiary)

        OnPrimary = getColor(config.theme.on_primary)
        OnSecondary = getColor(config.theme.on_secondary)
        OnTertiary = getColor(config.theme.on_tertiary)

        Text = getColor(config.theme.text_color)
        Hint = getColor(config.theme.hint_color)

        ContainerBg = getColor(config.theme.container_bg)
        SurfaceBg = getColor(config.theme.surface_bg)

        Success = getColor(config.theme.success)
        Error = getColor(config.theme.error)

        Log.d("AppConfig", "Config updated: $config")
    }

    private fun getColor(colorHex: String): Color {
        return try {
            Color(ColorUtils.setAlphaComponent(android.graphics.Color.parseColor(colorHex), 255))
        } catch (e: Exception) {
            Log.e("SnapThemeConfig", "Invalid color format: $colorHex", e)
            Color.Unspecified
        }
    }
}
data class ConfigResponse(
    val theme: ThemeConfig,
    val features: FeatureConfig
)

data class ThemeConfig(
    val primary: String,
    val on_primary: String,
    val secondary: String,
    val on_secondary: String,
    val tertiary: String,
    val on_tertiary: String,
    val text_color: String,
    val hint_color: String,
    val text_size_title: Int,
    val text_size_body: Int,
    val container_bg: String,
    val surface_bg: String,
    val success: String,
    val error: String
    ) {
    companion object {
        fun default() = ThemeConfig(
            primary = "#0A50A3",
            on_primary = "#FFFFFF",
            secondary = "#000000",
            on_secondary = "#FFFFFF",
            tertiary = "#FF03DAC6",
            on_tertiary = "#000000",
            text_color = "#000000",
            hint_color = "#757575",
            text_size_title = 16,
            text_size_body = 14,
            container_bg = "#F5F5F5",
            surface_bg = "#E0E0E0",
            success = "#008000",
            error="#990000"
        )
    }
}

data class FeatureConfig(
    val cart: Boolean,
    val reports: Boolean,
    val push_notifications: Boolean,
    val analytics: Boolean,
    val dark_mode: Boolean,
    val voice_search: Boolean
) {
    companion object {
        fun default() = FeatureConfig(
            cart = true,
            reports = false,
            push_notifications = true,
            analytics = true,
            dark_mode = false,
            voice_search = true
        )
    }
}

