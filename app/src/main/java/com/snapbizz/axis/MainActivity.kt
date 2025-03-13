package com.snapbizz.axis

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.snapbizz.axis.navigation.Navigation
import com.snapbizz.axis.navigation.Screen
import com.snapbizz.core.helpers.ConfigManager
import com.snapbizz.ui.snapComponents.SnapScaffold
import com.snapbizz.ui.theme.SnapbillingDDTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnapbillingDDTheme {
                    SnapScaffold {
                        Navigation(Screen.HOME)
                    }

            }
        }
    }
}

fun getRemoteConfig(application: Context) {
    val configManager = ConfigManager(application)
    CoroutineScope(Job() + Dispatchers.IO).launch {
        configManager.fetchConfig(ConfigManager.Source.FIREBASE)
    }
}
