package com.snapbizz.axis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.snapbizz.axis.navigation.Navigation
import com.snapbizz.axis.navigation.Screen
import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.ui.snapComponents.SnapScaffold
import com.snapbizz.ui.theme.SnapbillingDDTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var snapDataStore: SnapDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val systemUiController = rememberSystemUiController()
            var startDestination by remember { mutableStateOf<Screen?>(null) }

            LaunchedEffect(Unit) {
                startDestination = getStartingScreen()
            }
            SideEffect {
                systemUiController.setStatusBarColor(color = SnapThemeConfig.Primary)
            }
            SnapbillingDDTheme {
                val insets = WindowInsets.systemBars.asPaddingValues()
                var showSplash by remember { mutableStateOf(true) }
                Box(modifier = Modifier.fillMaxSize()) {
                    if (showSplash) {
                        SplashScreen {
                            showSplash = false
                        }
                    } else {
                        Navigation(Modifier.padding(insets), startDestination)
                    }
                }
            }
        }

    }

    private suspend fun getStartingScreen(): Screen {

        val isRegistered = snapDataStore.isStoreRegistrationComplete()
//        val isLoggedIn = snapDataStore.isUserLoggedIn()
        return if (isRegistered == true) {
            Screen.HOME
        } else {
            Screen.OTP
        }
    }
}





