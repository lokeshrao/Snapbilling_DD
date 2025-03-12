package com.snapbizz.axis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.snapbizz.ui.snapComponents.SnapButton
import com.snapbizz.ui.snapComponents.SnapText

@Composable
fun HomeScreen(onNavigateToRegister: (String) -> Unit) {
    Column {
        SnapText(
            text = "Home Screen",
            modifier = Modifier
        )
        Column {
            SnapButton(text = "Hello", onClick = {
                onNavigateToRegister("")
            })
            SnapText(
                text = "Hello Home Screen",
                modifier = Modifier.background(Color.Blue)
            )
        }
    }

}
