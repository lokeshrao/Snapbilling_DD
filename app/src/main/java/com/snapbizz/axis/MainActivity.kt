package com.snapbizz.axis

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.snapbizz.core.helpers.AppConfig
import com.snapbizz.core.helpers.ConfigManager
import com.snapbizz.ui.snapComponents.SnapButton
import com.snapbizz.ui.snapComponents.SnapEditText
import com.snapbizz.ui.snapComponents.SnapScaffold
import com.snapbizz.ui.snapComponents.SnapText
import com.snapbizz.ui.theme.SnapbillingDDTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnapbillingDDTheme {
                SnapScaffold(
                    title = "Android"
                ) {
                    Greeting("Android")
                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var context = LocalContext.current.applicationContext
    Column {
        SnapButton(text = "Hello", onClick = {getRemoteConfig(context)})
        SnapText(
            text = "Hello $name!",
            modifier = modifier.background(AppConfig.Primary)
        )
        SnapEditText(value = "hello",hint="hellohi ", onValueChange = {})
    }

}

fun getRemoteConfig(application: Context) {
    val configManager = ConfigManager(application)
    CoroutineScope(Job() + Dispatchers.IO).launch {
        configManager.fetchConfig(ConfigManager.Source.FIREBASE)
    }


}
