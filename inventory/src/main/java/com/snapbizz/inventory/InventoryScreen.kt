package com.snapbizz.inventory

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.snapbizz.core.utils.Dimens.paddingLarge
import com.snapbizz.core.utils.Dimens.paddingSmall
import com.snapbizz.ui.SnackbarManager
import com.snapbizz.ui.snapComponents.SnapButton
import com.snapbizz.ui.snapComponents.SnapEditText

@Composable
fun InventoryScreen(viewModel: InventoryViewModel = hiltViewModel()) {
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()
    LaunchedEffect(message) {
        if (message?.isNotEmpty() == true) {
            SnackbarManager.showSnackbar(message?:"")
            viewModel.clearError()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(paddingLarge), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SnapEditText(
                value = products?.productName?.value.toString(),
                label = "Product Name",
                keyboardType = KeyboardType.Text,
                onValueChange = { products?.productName?.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingSmall)
            )

            SnapEditText(
                value = products?.productCode?.value.toString(),
                label = "Product Id",
                keyboardType = KeyboardType.Phone,
                onValueChange = { products?.productCode?.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingSmall)
            )

            SnapEditText(
                value = products?.productMrp?.value.toString(),
                label = "Mrp",
                keyboardType = KeyboardType.Phone,
                onValueChange = { products?.productMrp?.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingSmall)
            )

            SnapEditText(
                value = products?.quantity?.value.toString(),
                label = "Stock",
                keyboardType = KeyboardType.Phone,
                onValueChange = { products?.quantity?.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingSmall)
            )
            SnapEditText(
                value = products?.productPP?.value.toString(),
                label = "PP",
                keyboardType = KeyboardType.Phone,
                onValueChange = { products?.productPP?.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingSmall)
            )
            SnapEditText(
                value = products?.productUom?.value.toString(),
                label = "UOM",
                keyboardType = KeyboardType.Text,
                onValueChange = { products?.productUom?.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingSmall)
            )
            SnapButton(
                text = "Save",
                onClick = {
                    viewModel.insertProduct()
                })
        }
    }
}