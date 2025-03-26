package com.snapbizz.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.common.config.models.ProductInfo
import com.snapbizz.core.database.entities.ProductPacks
import com.snapbizz.core.database.entities.global.GlobalProduct
import com.snapbizz.core.utils.Dimens.paddingSmall
import com.snapbizz.ui.SnackbarManager
import com.snapbizz.ui.snapComponents.SnapButton
import com.snapbizz.ui.snapComponents.SnapEditText
import com.snapbizz.ui.snapComponents.SnapPaginatedList
import com.snapbizz.ui.snapComponents.SnapText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InventoryScreen(viewModel: InventoryViewModel = hiltViewModel()) {
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var isNewProduct by rememberSaveable { mutableStateOf(false) }
    val message by viewModel.message.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val productInfo = viewModel.allProducts.collectAsLazyPagingItems()
    val coroutineScope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }
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
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            SnapEditText(
                value = searchQuery,
                onValueChange = { newQuery ->
                    searchQuery = newQuery
                    searchJob?.cancel()
                    searchJob = coroutineScope.launch {
                        delay(300)
                        viewModel.updateQuery(searchQuery)
                    }
                },
                label = "Search Products",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            SnapButton(
                text = "Add new Product",
                onClick = {
                    isNewProduct = true
                })

            if (isNewProduct.not()) {
                SnapPaginatedList(items = productInfo) { product ->
                    ProductItemInventory(product)
                }
            } else {
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
                        isNewProduct = false
                        viewModel.insertProduct()
                    })
            }
        }

    }
}

@Composable
fun ProductItemInventory(product: ProductInfo?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SnapThemeConfig.SurfaceBg)
            .padding(16.dp)
    ) {
        SnapText(text = product?.name.toString(), textAlign = TextAlign.Start)
        SnapText(text = "Price: ₹${product?.mrp ?: ""}")
    }
}