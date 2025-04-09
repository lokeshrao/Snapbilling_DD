package com.snapbizz.inventory.inventory.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.common.models.ProductInfo
import com.snapbizz.inventory.inventory.InventoryViewModel
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
    val isLoading by viewModel.isLoading.collectAsState()
    var isNewProduct by rememberSaveable { mutableStateOf(false) }
    val message by viewModel.message.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val productInfo = viewModel.allProducts.collectAsLazyPagingItems()
    val coroutineScope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }
    var productData: ProductInfo by remember { mutableStateOf(ProductInfo()) }
    LaunchedEffect(message) {
        if (message?.isNotEmpty() == true) {
            SnackbarManager.showSnackbar(message?:"")
            viewModel.clearError()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getInvoiceWithItems()
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
                    ProductItemInventory(product) {selectedProd->
                        if (selectedProd != null) {
                            productData = selectedProd
                        }
                        isNewProduct = true
                    }
                }
            } else {
                AddEditInventoryScreen(productData,onProductSave = {
                    viewModel.insertProduct(it)
                    isNewProduct = false
                })
            }
        }
    }
}

@Composable
fun ProductItemInventory(product: ProductInfo?, onClick: (ProductInfo?) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SnapThemeConfig.SurfaceBg)
            .padding(16.dp)
            .clickable{onClick(product)}
    ) {
        SnapText(text = product?.name.toString(), textAlign = TextAlign.Start)
        SnapText(text = "Price: ₹${product?.mrp ?: ""}")
    }
}