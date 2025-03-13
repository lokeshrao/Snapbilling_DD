package com.snapbizz.common.config

import android.graphics.Color.parseColor
import android.util.Log
import androidx.compose.ui.graphics.Color

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

        Primary = getColor(config.theme.primary_color)
        Secondary = getColor(config.theme.secondary_color)
        Tertiary = getColor(config.theme.tertiary_color)

        PrimaryBg = getColor(config.theme.primary_bg_color)
        SecondaryBg = getColor(config.theme.secondary_bg_color)
        TertiaryBg = getColor(config.theme.tertiary_bg_color)

        OnPrimary = getColor(config.theme.on_primary_color)
        OnSecondary = getColor(config.theme.on_secondary_color)
        OnTertiary = getColor(config.theme.on_tertiary_color)

        Text = getColor(config.theme.text_color)
        Hint = getColor(config.theme.hint_color)

        Log.d("AppConfig", "Config updated: $config")
    }

    private fun getColor(colorHex: String): Color = Color(parseColor(colorHex))
}

data class ConfigResponse(
    val theme: ThemeConfig,
    val features: FeatureConfig
)

data class ThemeConfig(
    val primary_color: String,
    val secondary_color: String,
    val tertiary_color: String,
    val primary_bg_color: String,
    val secondary_bg_color: String,
    val tertiary_bg_color: String,
    val on_primary_color: String,
    val on_secondary_color: String,
    val on_tertiary_color: String,
    val text_color: String,
    val hint_color: String,
    val text_size_title: Int,
    val text_size_body: Int
) {
    companion object {
        fun default() = ThemeConfig(
            primary_color = "#0A50A3",
            secondary_color = "#000000",
            tertiary_color = "#FF03DAC6",
            primary_bg_color = "#FFFFFF",
            secondary_bg_color = "#F5F5F5",
            tertiary_bg_color = "#E0E0E0",
            on_primary_color = "#FFFFFF",
            on_secondary_color = "#FFFFFF",
            on_tertiary_color = "#000000",
            text_color = "#000000",
            hint_color = "#757575",
            text_size_title = 16,
            text_size_body = 14
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
