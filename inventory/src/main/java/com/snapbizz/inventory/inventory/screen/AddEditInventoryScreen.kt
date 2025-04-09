package com.snapbizz.inventory.inventory.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.snapbizz.common.models.ProductInfo
import com.snapbizz.core.utils.Dimens.paddingSmall
import com.snapbizz.ui.snapComponents.SnapButton
import com.snapbizz.ui.snapComponents.SnapEditText

@Composable
fun AddEditInventoryScreen(
    product: ProductInfo? = null,
    onProductSave: (ProductInfo) -> Unit
) {
    var product by remember { mutableStateOf(product?: ProductInfo()) }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            SnapEditText(
                value = product.name?:"",
                label = "Product Name",
                keyboardType = KeyboardType.Text,
                onValueChange = { product = product.copy(name = it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingSmall)
            )

            SnapEditText(
                value = product.productCode?:"",
                label = "Product Id",
                keyboardType = KeyboardType.Phone,
                onValueChange = { product = product.copy(productCode = it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingSmall)
            )

            SnapEditText(
                value = product.mrp?.toString()?:"",
                label = "Mrp",
                keyboardType = KeyboardType.Phone,
                onValueChange = { product = product.copy(mrp = it.toDoubleOrNull()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingSmall)
            )

            SnapEditText(
                value = product.inventoryQuantity?.toString()?:"",
                label = "Stock",
                keyboardType = KeyboardType.Phone,
                onValueChange = { product = product.copy(inventoryQuantity = it.toIntOrNull()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingSmall)
            )
            SnapEditText(
                value = product.purchasePrice?.toString()?:"",
                label = "PP",
                keyboardType = KeyboardType.Phone,
                onValueChange = { product = product.copy(purchasePrice = it.toDoubleOrNull()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingSmall)
            )
            SnapEditText(
                value = product.uom?:"",
                label = "UOM",
                keyboardType = KeyboardType.Text,
                onValueChange = {  product = product.copy(uom = it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingSmall)
            )
            SnapButton(
                text = "Save",
                onClick = {
                    onProductSave(product)
                })
        }
    }
}