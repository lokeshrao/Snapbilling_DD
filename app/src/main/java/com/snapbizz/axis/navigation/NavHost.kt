package com.snapbizz.axis.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.snapbizz.axis.HomeScreen
import com.snapbizz.onboarding.OtpScreen

@Composable
fun Navigation(startingDestination: Screen) {
    val navController = rememberNavController()
    var startingPage = startingDestination
    NavHost(navController = navController, startDestination = startingPage.route) {
        composable(Screen.OTP.route) {
            OtpScreen()
        }
        composable(Screen.HOME.route) {
            HomeScreen {
                navController.navigate(Screen.OTP.route)
            }
        }
    }
}