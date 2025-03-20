package com.snapbizz.onboarding.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.snapbizz.core.utils.Dimens.paddingLarge
import com.snapbizz.core.utils.Dimens.paddingSmall
import com.snapbizz.core.utils.showMessage
import com.snapbizz.onboarding.OnBoardingViewModel
import com.snapbizz.onboarding.R
import com.snapbizz.ui.snapComponents.AxisBanner
import com.snapbizz.ui.snapComponents.ImageWithText
import com.snapbizz.ui.snapComponents.SnapButton
import com.snapbizz.ui.snapComponents.SnapEditText
import com.snapbizz.ui.snapComponents.SnapOtpField
import com.snapbizz.ui.snapComponents.SnapSlider
import com.snapbizz.ui.snapComponents.SnapText
import com.snapbizz.ui.theme.SnapTextStyle

@Composable
fun OtpScreen(
    onNavigateToRegister: (String) -> Unit, viewModel: OnBoardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getDeviceId(context)
    }

    LaunchedEffect(uiState) {
        if (uiState.isVerified) {
            val storeJson = Gson().toJson(viewModel.storeDetails.value)
            onNavigateToRegister(storeJson)
        }
    }

    LaunchedEffect(uiState) {
        if (uiState.message?.isNotEmpty() == true) {
            context.showMessage(uiState.message)
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AxisBanner()
            SnapSlider(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(vertical = 16.dp),
                isAutoSlide = true,
                pages = listOf(
                    { ImageWithText(R.drawable.deals, "Page 1") },
                    { ImageWithText(R.drawable.quick, "Page 2") },
                    { ImageWithText(R.drawable.report, "Page 3") },
                    { ImageWithText(R.drawable.secure, "Page 4") })
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
                    .background(Color.LightGray)
                    .padding(paddingLarge)
            ) {
                SnapEditText(
                    value = uiState.phoneNo,
                    label = "Enter Store Phone Number",
                    keyboardType = KeyboardType.Phone,
                    onValueChange = { if (it.length <= 10) viewModel.setPhoneNo(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = paddingSmall)
                )

                if (uiState.isOtpSent) {
                    SnapOtpField(
                        otpValue = uiState.otp,
                        onOtpChange ={ viewModel.setOtp(it) } ,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = paddingSmall)
                    )
                }

                SnapButton(
                    isLoading = uiState.isLoading,
                    text = if (uiState.isOtpSent) "Verify" else "Get OTP",
                    onClick = {
                        viewModel.onButtonClick()
                    })

                Spacer(modifier = Modifier.weight(1f))

                SnapText(
                    text = "Device ID: ${uiState.deviceId}",
                    fontSize = SnapTextStyle.Default.fontSize,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    color = Color.Gray
                )
            }
        }
    }
}