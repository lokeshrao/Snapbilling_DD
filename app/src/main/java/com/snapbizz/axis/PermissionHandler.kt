package com.snapbizz.axis

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun PermissionHandler(
    permissions: List<String>,
    onAllPermissionsGranted: () -> Unit,
    onPermissionsDenied: (deniedPermissions: List<String>) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val deniedPermissions = result.filterValues { !it }.keys.toList()
        if (deniedPermissions.isEmpty()) {
            onAllPermissionsGranted()
        } else {
            onPermissionsDenied(deniedPermissions)
        }
    }

    LaunchedEffect(Unit) {
        val deniedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isEmpty()) {
            onAllPermissionsGranted()
        } else {
            launcher.launch(deniedPermissions.toTypedArray())
        }
    }
}

