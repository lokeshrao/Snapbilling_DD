package com.snapbizz.axis

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.core.datastore.di.SnapStoreProvider
import com.snapbizz.core.helpers.Source
import com.snapbizz.core.helpers.fetchAndApplyConfig
import com.snapbizz.ui.R
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SplashScreen(onTimeOut: () -> Unit) {
    val context = LocalContext.current
    var isPermissionGranted by remember { mutableStateOf(false) }
    var isPermissionDenied by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        (context as? ComponentActivity)?.window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
        }
    }

    PermissionHandler(
        permissions = listOf(
            Manifest.permission.READ_PHONE_STATE
        ), onAllPermissionsGranted = {
            isPermissionGranted = true
        }, onPermissionsDenied = { deniedPermissions ->
            coroutineScope.launch {
                if (Manifest.permission.READ_PHONE_STATE in deniedPermissions) {
                    Toast.makeText(
                        context,
                        "READ_PHONE_STATE denied. Check privacy settings.",
                        Toast.LENGTH_LONG
                    ).show()
                    isPermissionDenied = true
                    (context as? ComponentActivity)?.finish()
                }
            }
        })

    // 🔹 Fullscreen Box containing the Splash Image
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash),
            contentDescription = "Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }

    if (isPermissionGranted) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionHandler(listOf(Manifest.permission.POST_NOTIFICATIONS), {}, {})
        }
        val snapDataStore: SnapDataStore = EntryPointAccessors.fromApplication(
            context.applicationContext, SnapStoreProvider::class.java
        ).snapDataStore
        getRemoteConfig(coroutineScope, snapDataStore) { onTimeOut() }
    }
}

fun getRemoteConfig(
    coroutineScope: CoroutineScope, snapDataStore: SnapDataStore, onTimeOut: () -> Unit
) {
    coroutineScope.launch {
        withContext(Dispatchers.IO) {
            fetchAndApplyConfig(snapDataStore, Source.FIREBASE)
            onTimeOut()
        }
    }
}
