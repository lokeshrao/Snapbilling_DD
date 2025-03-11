package com.snapbizz.axis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.snapbizz.ui.snapComponents.SnapButton
import com.snapbizz.ui.snapComponents.SnapScaffold
import com.snapbizz.ui.snapComponents.SnapText
import com.snapbizz.ui.theme.SnapbillingDDTheme

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
    Column {
        SnapButton(text = "Hello", onClick = {})
        SnapText(
            text = "Hello $name!",
            modifier = modifier
        )
    }

}