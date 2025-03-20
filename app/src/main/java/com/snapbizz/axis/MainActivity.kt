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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.snapbizz.axis.navigation.Navigation
import com.snapbizz.axis.navigation.Screen
import com.snapbizz.ui.snapComponents.SnapScaffold
import com.snapbizz.ui.snapComponents.SplashScreen
import com.snapbizz.ui.theme.SnapbillingDDTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true
        window.statusBarColor = getColor(R.color.transparent)
        setContent {
            SnapbillingDDTheme {
                val insets = WindowInsets.systemBars.asPaddingValues()

                var showSplash by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(2000)
                    showSplash = false
                }

                Box(modifier = Modifier.fillMaxSize().padding(insets)) {
                    if (showSplash) {
                        SplashScreen()
                    } else {
                        SnapScaffold {
                            Navigation(Screen.OTP)
                        }
                    }
                }
            }
        }
    }
}





