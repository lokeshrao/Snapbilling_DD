package com.snapbizz.axis.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.snapbizz.common.config.SnapThemeConfig
import com.snapbizz.snapbillingv2.R
import com.snapbizz.ui.snapComponents.BottomBarItem
import com.snapbizz.ui.snapComponents.SnapButton
import com.snapbizz.ui.snapComponents.SnapScaffoldWithDrawer
import com.snapbizz.ui.snapComponents.SnapText

@Composable
fun HomeScreenWithLayout() {
    val bottomItems = listOf(
        BottomBarItem(R.drawable.baseline_home_filled_24, "Home"),
        BottomBarItem(R.drawable.baseline_home_filled_24, "Promotions"),
        BottomBarItem(R.drawable.baseline_home_filled_24, "Customers"),
        BottomBarItem(R.drawable.baseline_home_filled_24, "Reports"),
        BottomBarItem(R.drawable.baseline_home_filled_24, "More")
    )
    SnapScaffoldWithDrawer(
        drawerItems = listOf(
            "Settings" to com.snapbizz.ui.R.drawable.baseline_check_24,
            "Reports" to com.snapbizz.ui.R.drawable.baseline_check_24,
            "Logout" to com.snapbizz.ui.R.drawable.baseline_check_24
        ),
        bottomItems = bottomItems,
        onDrawerItemClick = { item ->
            println("Drawer clicked: $item")
        },
        onBottomItemClick = { item ->
            println("Bottom bar clicked: $item")
        } ,
        bottomSelectedField = "Home"
    ) { padding ->
        HomeScreen(modifier = Modifier.padding(padding))
    }
}

@Composable
fun HomeScreen(modifier: Modifier,viewModel: HomeViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SnapThemeConfig.ContainerBg)
            .padding(16.dp)
    ) {
        SnapText(
            text = "Welcome 👋",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))
        SnapButton(text = "fetch", onClick = { viewModel.getAppKeys() })
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomIconTile(iconRes = R.drawable.baseline_home_filled_24, label = "Sale") {
                // Handle click
            }
            CustomIconTile(iconRes = R.drawable.baseline_home_filled_24, label = "Return") {}
            CustomIconTile(iconRes = R.drawable.baseline_home_filled_24, label = "Txn") {}
            CustomIconTile(iconRes = R.drawable.baseline_home_filled_24, label = "Refund") {}
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomIconTile(iconRes = R.drawable.baseline_home_filled_24, label = "Close Day") {}
            CustomIconTile(iconRes = R.drawable.baseline_home_filled_24, label = "Reports") {}
            CustomIconTile(iconRes = R.drawable.baseline_home_filled_24, label = "Settings") {}
            CustomIconTile(iconRes = R.drawable.baseline_home_filled_24, label = "Logout") {}
        }
    }
}

@Composable
fun CustomIconTile(iconRes: Int, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(72.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier
                .size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        SnapText(
            text = label,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

