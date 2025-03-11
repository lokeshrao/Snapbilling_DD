package com.snapbizz.ui.snapComponents
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun SnapScaffold(
    title: String = "SnapBizz",
    showTopBar: Boolean = true,
    showBottomBar: Boolean = false,
    showFab: Boolean = false,
    onFabClick: () -> Unit = {},
    drawerContent: (@Composable () -> Unit)? = null,
    bottomBarContent: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        drawerContent?.let {
                            IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
//                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                bottomBarContent?.invoke()
            }
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(onClick = onFabClick) {
                    Text("+")
                }
            }
        },
        drawerContent = {
            drawerContent?.invoke()
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                content(paddingValues)
            }
        }
    )
}
