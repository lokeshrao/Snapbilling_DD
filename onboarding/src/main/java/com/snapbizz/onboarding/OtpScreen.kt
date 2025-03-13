package com.snapbizz.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.snapbizz.ui.snapComponents.SnapText
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.snapbizz.ui.snapComponents.SnapButton

@Composable
fun OtpScreen(otpViewModel: OtpViewModel = hiltViewModel()) {
    Column {
        SnapText(
            modifier = Modifier,
            text = "OTP Screen",
            color = Color.Blue
        )
        SnapButton("Hi" , onClick = { otpViewModel.getData() })
    }
}