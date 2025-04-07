package com.snapbizz.billing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun QuickPayScreen(viewModel: QuickPayViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.getAppKeys()
    }
}