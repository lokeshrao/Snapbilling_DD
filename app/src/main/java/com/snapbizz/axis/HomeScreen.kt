package com.snapbizz.axis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.snapbizz.core.helpers.LogModule
import com.snapbizz.core.helpers.LogPriority
import com.snapbizz.core.helpers.SnapLogger
import com.snapbizz.ui.SnackbarManager
import com.snapbizz.ui.snapComponents.SnapButton
import com.snapbizz.ui.snapComponents.SnapText

@Composable
fun HomeScreen(onNavigateToRegister: (String) -> Unit) {
    Column {
        SnapText(
            text = "Home Screen", modifier = Modifier
        )
        Column {
            SnapButton(text = "Hello", onClick = {
                SnapLogger.log("MyService", "Logging from a Service", LogModule.HOME, LogPriority.HIGH)
                SnapLogger.log("MyService", "Logging from a Service", LogModule.HOME)

                    try {
                        try {
                            throw IllegalArgumentException("Inner Exception Occurred")
                        } catch (inner: Exception) {
                            throw IllegalStateException("Outer Exception Occurred", inner)
                        }
                    } catch (outer: Exception) {
                        outer.printStackTrace()
                        SnapLogger.logException("MyService", LogModule.HOME, outer)
                    }
                //SnackbarManager.showSnackbar("Hello Hi ","Retry",{})
                onNavigateToRegister("")
            })
            SnapText(
                text = "Hello Home Screen", modifier = Modifier.background(Color.Blue)
            )
        }
    }

}
