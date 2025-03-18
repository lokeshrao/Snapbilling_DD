package com.snapbizz.onboarding.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.snapbizz.common.config.models.StoreDetailsResponse
import com.snapbizz.core.utils.Dimens.paddingMedium
import com.snapbizz.core.utils.Dimens.paddingSmall
import com.snapbizz.core.utils.showMessage
import com.snapbizz.onboarding.OnBoardingViewModel
import com.snapbizz.onboarding.R
import com.snapbizz.ui.snapComponents.SnapButton
import com.snapbizz.ui.snapComponents.SnapEditText
import com.snapbizz.ui.snapComponents.SnapText
import com.snapbizz.ui.theme.SnapTextStyle

@Composable
fun RegisterScreen(userJson: String, onNavigateToLogin: () -> Unit) {
    val viewModel = hiltViewModel<OnBoardingViewModel>()
    val isLoading by viewModel.loading.collectAsState()
    LaunchedEffect(userJson) {
        viewModel.setLoader(true)
        userJson.let { Gson().fromJson(it, StoreDetailsResponse::class.java) }.let { storeDetails ->
            viewModel.setStoreDetails(storeDetails)
            viewModel.setLoader(false)
        }
    }
    val storeDetails by viewModel.storeDetails.collectAsState()
    val error by viewModel.error.collectAsState()
    //val syncMessages by viewModel.syncMessages.collectAsState()

    val scrollState = rememberScrollState()
    var isAgreed by rememberSaveable { mutableStateOf(false) }
    val posID by viewModel.posId.collectAsState()
    var showAgreementDialog by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    LaunchedEffect(error) {
        error?.let {
            context.showMessage(it)
            viewModel.clearError()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingMedium)
    ) {
        SnapText(
            text = stringResource(id = R.string.registration_title),
            fontSize = SnapTextStyle.Default.fontSize,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = paddingMedium),
            textAlign = TextAlign.Center
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            SectionTitle(stringResource(id = R.string.owner_details))

            InfoField(label = stringResource(id = R.string.owner_name), value = storeDetails?.retailerDetails?.name)
            InfoField(label = stringResource(id = R.string.email), value = storeDetails?.retailerDetails?.email)

            SectionTitle(stringResource(id = R.string.store_details))

            InfoField(label = stringResource(id = R.string.address), value = storeDetails?.storeDetails?.address)
            InfoField(label = stringResource(id = R.string.store_name), value = storeDetails?.storeDetails?.name)
            InfoField(
                label = stringResource(id = R.string.store_phone_number), value = storeDetails?.storeDetails?.phone?.toString()
            )
            InfoField(label = stringResource(id = R.string.address), value = storeDetails?.storeDetails?.address)

            Row(modifier = Modifier.fillMaxWidth()) {
                InfoField(
                    label = stringResource(id = R.string.zip_code),
                    value = storeDetails?.storeDetails?.pincode?.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = paddingSmall)
                )
                InfoField(
                    label = stringResource(id = R.string.gstin_number),
                    value = storeDetails?.storeDetails?.tin?.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = paddingSmall)
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                InfoField(
                    label = stringResource(id = R.string.state),
                    value = storeDetails?.storeDetails?.state,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = paddingSmall)
                )
                InfoField(
                    label = stringResource(id = R.string.city),
                    value = storeDetails?.storeDetails?.city,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = paddingSmall)
                )
            }

            InfoField(label = stringResource(id = R.string.pos_id), value = posID.toString())
            Row {
                //Checkbox(checked = isAgreed, onCheckedChange = { showAgreementDialog = true })
                Spacer(modifier = Modifier.width(paddingSmall))
                SnapText(text = stringResource(id = R.string.check_agreement))
//                if (showAgreementDialog) {
//                    HPHDialog(header = stringResource(id = R.string.terms),
//                        message = stringResource(id = R.string.agreement_string),
//                        onDismissRequest = { showAgreementDialog = false },
//                        confirmButtonText = stringResource(id = R.string.accept),
//                        dismissButtonText = stringResource(id = R.string.reject),
//                        onConfirm = { isAgreed = true },
//                        onDismiss = { isAgreed = false })
//                }
            }
            SnapButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = paddingSmall),
                text = stringResource(id = R.string.proceed),
                onClick = {
//                    HPHCommonUtils.isInternetAvailable(context) {
                        viewModel.doDownloadSync {
//                            onNavigateToLogin()
                        }
//                    }
                }
            )
        }
    }
//    if (isLoading) {
//        HPHProgressDialog(syncMessages)
//    }
}

@Composable
fun SectionTitle(title: String) {
    SnapText(
        text = title,
        fontSize = SnapTextStyle.Default.fontSize,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = paddingSmall)
    )
}

@Composable
fun InfoField(label: String, value: String?, modifier: Modifier = Modifier) {
    SnapEditText(
        value = value ?: "",
        enabled = false,
        label = label,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = paddingSmall)
    )
}

