package com.snapbizz.axis.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.snapbizz.axis.HomeScreen
import com.snapbizz.inventory.InventoryScreen
import com.snapbizz.inventory.screen.GlobalProductScreen
import com.snapbizz.onboarding.registration.OtpScreen
import com.snapbizz.onboarding.registration.RegisterScreen

@Composable
fun Navigation(startingDestination: Screen) {
    val navController = rememberNavController()
    var startingPage = startingDestination
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
            HomeScreen {
                navController.navigate(Screen.OTP.route)
            }
        }
        composable(Screen.REGISTER.route) { backStackEntry ->
            RegisterScreen(navigateForward = {
                navController.navigate(Screen.LOGIN.route) {
                    popUpTo(Screen.REGISTER.route) { inclusive = true }
                    launchSingleTop = true
                    startingPage = Screen.LOGIN
                }
            })
        }
        composable(Screen.INVENTORY.route) { backStackEntry ->
            GlobalProductScreen()
//            InventoryScreen()
        }
    }
}