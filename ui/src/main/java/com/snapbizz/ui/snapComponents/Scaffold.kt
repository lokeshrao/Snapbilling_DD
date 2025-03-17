package com.snapbizz.ui.snapComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.snapbizz.ui.SnackbarManager
import kotlinx.coroutines.launch

@Composable
fun SnapScaffold(
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = SnackbarManager.snackbarHostState) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                content(paddingValues)
            }
        })
}
