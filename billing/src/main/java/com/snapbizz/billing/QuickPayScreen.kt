package com.snapbizz.billing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.common.models.PaymentData
import com.snapbizz.core.utils.Dimens.paddingMedium
import com.snapbizz.core.utils.Dimens.paddingSmall
import com.snapbizz.core.utils.Dimens.paddingTooSmall
import com.snapbizz.ui.snapComponents.SnapButton
import com.snapbizz.ui.snapComponents.SnapEditText
import java.util.Date

@Composable
fun QuickPayScreen(viewModel: QuickPayViewModel = hiltViewModel()) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .imePadding()) {

        Column(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .background(SnapThemeConfig.SurfaceBg)
            .clip(RoundedCornerShape(0.dp,0.dp,20.dp,20.dp))
        ) {
            SnapEditText(
                value = "",
                label = "Customer Tagging",
                keyboardType = KeyboardType.Phone,
                leadingIconResId = com.snapbizz.ui.R.drawable.baseline_check_24,
                onValueChange = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingMedium)
            )
        }

        Column(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(paddingMedium)
        ) {
            SnapEditText(
                value = "",
                label = "Amount",
                keyboardType = KeyboardType.Phone,
                leadingIconResId = com.snapbizz.ui.R.drawable.baseline_check_24,
                onValueChange = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingTooSmall)
            )
            SnapEditText(
                value = "",
                label = "Remarks",
                keyboardType = KeyboardType.Phone,
                leadingIconResId = com.snapbizz.ui.R.drawable.baseline_check_24,
                onValueChange = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingTooSmall)
            )
            SnapButton(
                modifier = Modifier
                    .padding(paddingMedium)
                    .align(Alignment.CenterHorizontally),
                text = "Collect payment",
                onClick = {
                    keyboardController?.hide()
                    PaymentActivity.newIntent(context, PaymentData(10.10, Date().time.toString(), "", "", "")).let {
                        context.startActivity(it)
                    }
//                    viewModel.onButtonClick()
                }
            )
        }
    }
}