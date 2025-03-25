package com.snapbizz.ui.snapComponents
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@Composable
fun <T : Any> SnapPaginatedList(
    items: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit
) {
    when (items.loadState.refresh) {
        is LoadState.Error -> {
            val error = (items.loadState.refresh as LoadState.Error).error
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                SnapText(text = "Error: ${error.localizedMessage ?: "Unable to fetch data"}")
            }
        }

        is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                SnapCircularIndicator(modifier = Modifier.padding(16.dp))
            }
        }

        else -> {
            if (items.itemCount == 0) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SnapText(text = "No data found", modifier = Modifier.padding(16.dp))
                }
            } else {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items.itemCount) { index ->
                        val item = items[index]
                        if (item != null) {
                            itemContent(item)
                        }
                    }
                    if (items.loadState.append is LoadState.Loading) {
                        item {
                            SnapCircularIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
