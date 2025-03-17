package com.snapbizz.ui

import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object SnackbarManager {
    val snackbarHostState = SnackbarHostState()
    private val scope: CoroutineScope = MainScope()

    fun showSnackbar(message: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel
            )
            if (result == SnackbarResult.ActionPerformed) {
                onAction?.invoke()
            }
        }
    }
}
