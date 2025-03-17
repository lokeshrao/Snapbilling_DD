package com.snapbizz.onboarding.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.snapbizz.core.utils.Dimens.paddingLarge
import com.snapbizz.core.utils.Dimens.paddingMedium
import com.snapbizz.core.utils.Dimens.paddingSmall
import com.snapbizz.core.utils.showMessage
import com.snapbizz.onboarding.OnBoardingViewModel
import com.snapbizz.ui.snapComponents.SnapButton
import com.snapbizz.ui.snapComponents.SnapEditText
import com.snapbizz.ui.snapComponents.SnapText
import com.snapbizz.ui.theme.SnapTextStyle

@Composable
fun OtpScreen(onNavigateToRegister: (String) -> Unit,viewModel: OnBoardingViewModel = hiltViewModel()) {
    val isOtpSent by viewModel.isOtpSent.collectAsState()
    val deviceId by viewModel.deviceId.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.error.collectAsState()
    val isVerified by viewModel.isVerified.collectAsState()
    val phoneNo by viewModel.phoneNo.collectAsState()
    val otp by viewModel.otp.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getDeviceId(context)
    }

    LaunchedEffect(isVerified) {
        if (isVerified) {
            onNavigateToRegister(Gson().toJson(viewModel.storeDetails.value))
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            context.showMessage(it)
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .padding(paddingMedium).weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SnapEditText(
                value = phoneNo,
                label = "Enter Phone",
                onValueChange = {
                    if (it.length <= 10) {
                        viewModel.setPhoneNo(it)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingSmall)
            )

            if (isOtpSent) {
                SnapEditText(
                    value = otp,
                    label = "Enter Otp",
                    onValueChange = { viewModel.setOtp(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = paddingSmall)
                )
            }

            SnapButton(
                text = if (isOtpSent) "Verify" else "Get Otp",
                onClick = {
                        if (isOtpSent) {
                            viewModel.onVerifyOtp(phoneNo, otp, deviceId)
                        } else {
                            viewModel.sendOtp(phoneNo, deviceId)
                        }

                }
            )
        }
        SnapText(
            text = "Device ID: $deviceId",
            fontSize = SnapTextStyle.Default.fontSize,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingLarge),
            textAlign = TextAlign.Left,
        )
    }
}