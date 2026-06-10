package com.example.jetpack1.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetpack1.common.AppSnackbarHost
import com.example.jetpack1.language.LanguageScreen
import com.example.jetpack1.screens.AddBudget.BudgetScreen
import com.example.jetpack1.screens.Dashboard.DashboardScreen
import com.example.jetpack1.screens.Dashboard.NotificationsScreen
import com.example.jetpack1.screens.Dashboard.ProfileScreen
import com.example.jetpack1.screens.Login.loginsignupScreen.LoginSignUpScreen
import com.example.jetpack1.screens.Login.login.LoginScreen
import com.example.jetpack1.screens.Login.signup.RegisterScreen
import com.example.jetpack1.screens.customize.CustomizeScreen
import com.example.jetpack1.screens.splashScreen.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    Scaffold(
        snackbarHost = {
            AppSnackbarHost()
        }
    ) {paddingValues ->
        NavHost(navController, startDestination = navroute.Splash.route) {
            composable(navroute.Splash.route) {
                SplashScreen(navController)
            }
            composable(navroute.loginsignup.route) {
                LoginSignUpScreen(navController)
            }
            composable(
                navroute.Login.route + "?email={email}",
                arguments = listOf(
                    navArgument("email") {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
            ) { backStackEntry ->
                val emailarg = backStackEntry.arguments?.getString("email") ?: ""
                LoginScreen(
                    navController,
                    emailarg = emailarg
                )
            }
            composable(navroute.signup.route) {
                RegisterScreen(navController)
            }
            composable(navroute.Dashboard.route) {
                DashboardScreen(navController)
            }
            composable(navroute.Profile.route) {
                CustomizeScreen(navController)
            }
            composable(navroute.notification.route) {
                NotificationsScreen(navController)
            }
            composable(navroute.language.route) {
                LanguageScreen(navController)
            }
            composable(navroute.addbudget.route) {
                BudgetScreen(navController)
            }
            composable(navroute.customize.route) {
                CustomizeScreen(navController)
            }

        }
    }
}

