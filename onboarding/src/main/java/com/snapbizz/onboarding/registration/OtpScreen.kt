package com.snapbizz.onboarding.registration

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Checkbox
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.ban.otptextfield.SnapOtpField
import com.google.gson.Gson
import com.snapbizz.core.utils.Dimens.paddingLarge
import com.snapbizz.core.utils.Dimens.paddingSmall
import com.snapbizz.core.utils.showMessage
import com.snapbizz.onboarding.OnBoardingViewModel
import com.snapbizz.onboarding.R
import com.snapbizz.ui.SnackbarManager
import com.snapbizz.ui.snapComponents.AxisBanner
import com.snapbizz.ui.snapComponents.ImageWithText
import com.snapbizz.ui.snapComponents.SnapButton
import com.snapbizz.ui.snapComponents.SnapCheckBox
import com.snapbizz.ui.snapComponents.SnapDialog
import com.snapbizz.ui.snapComponents.SnapEditText
import com.snapbizz.ui.snapComponents.SnapSlider
import com.snapbizz.ui.snapComponents.SnapText
import com.snapbizz.ui.theme.SnapTextStyle

@Composable
fun OtpScreen(
    onNavigateToRegister: (String) -> Unit, viewModel: OnBoardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var isAgreed by rememberSaveable { mutableStateOf(false) }
    var showAgreementDialog by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        (context as? ComponentActivity)?.window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
        }
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
            SnackbarManager.showSnackbar(uiState.message?:"")
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
                    { ImageWithText(R.drawable.deals, "Keep Your Customers Updated with best deals") },
                    { ImageWithText(R.drawable.quick, "Quick & Easy Billing for Your Store") },
                    { ImageWithText(R.drawable.report, "Stay Updated with Report & Analytics") },
                    { ImageWithText(R.drawable.secure, "Fast and Secure Payments in One Tap") })
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
                        otpText = uiState.otp,
                        onOtpTextChange = { value, otpInputFilled ->
                            viewModel.setOtp(value)
                        } ,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = paddingSmall)
                    )
                    SnapText(modifier = Modifier.padding(vertical = 8.dp) , text = "\uD83D\uDEE1\uFE0F Your Store Details are Secure")
                    Row(modifier = Modifier.padding(bottom = 8.dp)) {
                        SnapCheckBox(isChecked = isAgreed, onCheckedChange = { showAgreementDialog = true })
                        Spacer(modifier = Modifier.width(paddingSmall))
                        SnapText(textAlign = TextAlign.Start,text = stringResource(id = R.string.check_agreement))
                        if (showAgreementDialog) {
                            SnapDialog(header = stringResource(id = R.string.terms),
                                message = stringResource(id = R.string.agreement_string),
                                onDismissRequest = { showAgreementDialog = false },
                                confirmButtonText = stringResource(id = R.string.accept),
                                dismissButtonText = stringResource(id = R.string.reject),
                                onConfirm = { isAgreed = true },
                                onDismiss = { isAgreed = false })
                        }
                    }
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