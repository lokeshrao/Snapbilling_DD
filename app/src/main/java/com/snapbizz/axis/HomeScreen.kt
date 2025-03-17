package com.snapbizz.axis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.snapbizz.core.logger.PRIORITY
import com.snapbizz.core.logger.SnapLogger
import com.snapbizz.core.logger.SnapLoggerEntryPoint
import com.snapbizz.ui.snapComponents.SnapButton
import com.snapbizz.ui.snapComponents.SnapText
import dagger.hilt.android.EntryPointAccessors

@Composable
fun HomeScreen(onNavigateToRegister: (String) -> Unit) {
    val applicationContext = LocalContext.current.applicationContext
    Column {
        SnapText(
            text = "Home Screen", modifier = Modifier
        )
        Column {
            SnapButton(text = "Hello", onClick = {
                val entryPoint = EntryPointAccessors.fromApplication(
                    applicationContext, SnapLoggerEntryPoint::class.java
                )
                val snapLogger = entryPoint.getSnapLogger()
                snapLogger.log(PRIORITY.HIGH, "MyService", "onCreate", "Logging from a Service")
                snapLogger.log(PRIORITY.LOW, "MyService", "onCreate", "Logging from a Service")
                onNavigateToRegister("")
            })
            SnapText(
                text = "Hello Home Screen", modifier = Modifier.background(Color.Blue)
            )
        }
    }

}
