package com.snapbizz.axis.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.snapbizz.axis.home.HomeScreen
import com.snapbizz.billing.QuickPayScreen
import com.snapbizz.axis.home.HomeScreenWithLayout
import com.snapbizz.billing.PaymentActivity
import com.snapbizz.common.models.PaymentData
import com.snapbizz.onboarding.registration.OtpScreen
import com.snapbizz.onboarding.registration.RegisterScreen

@Composable
fun Navigation(startingDestination: Screen?) {
    val navController = rememberNavController()
    var startingPage = startingDestination ?: Screen.OTP
    NavHost(navController = navController, startDestination = startingPage.route) {
        composable(Screen.OTP.route) {
            OtpScreen(onNavigateToRegister = {
                navController.navigate(Screen.REGISTER.route) {
                    popUpTo(Screen.OTP.route) { inclusive = true }
                    launchSingleTop = true
                }
            })
        }
        composable(Screen.HOME.route) {
            HomeScreenWithLayout()
        }
        composable(Screen.REGISTER.route) { backStackEntry ->
            RegisterScreen(navigateForward = {
                navController.navigate(Screen.HOME.route) {
                    popUpTo(Screen.REGISTER.route) { inclusive = true }
                    launchSingleTop = true
                }
            })
        }
        composable(Screen.INVENTORY.route) { backStackEntry ->
//            GlobalProductScreen()
//            InventoryScreen()
            QuickPayScreen()
        }
    }
}