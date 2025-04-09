package com.snapbizz.inventory.inventory.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.core.database.entities.global.GlobalProduct
import com.snapbizz.ui.snapComponents.SnapEditText
import com.snapbizz.ui.snapComponents.SnapPaginatedList
import com.snapbizz.ui.snapComponents.SnapText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GlobalProductScreen(viewModel: GlobalProductViewModel = hiltViewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    val products: LazyPagingItems<GlobalProduct> = viewModel.allProducts.collectAsLazyPagingItems()
    val coroutineScope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }

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

        SnapPaginatedList(items = products) { product ->
            ProductItem(product)
        }
    }
}

@Composable
fun ProductItem(product: GlobalProduct?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SnapThemeConfig.SurfaceBg)
            .padding(16.dp)
    ) {
        SnapText(text = product?.name ?: "", textAlign = TextAlign.Start)
        SnapText(text = "Price: ₹${product?.mrp ?: ""}")
    }
}
