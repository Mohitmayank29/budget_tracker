package com.example.jetpack1.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpack1.language.LanguageScreen
import com.example.jetpack1.screens.Dashboard.DashboardScreen
import com.example.jetpack1.screens.Login.LoginScreen
import com.example.jetpack1.screens.Dashboard.NotificationsScreen
import com.example.jetpack1.screens.Dashboard.ProfileScreen
import com.example.jetpack1.screens.Login.RegisterScreen
import com.example.jetpack1.screens.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = navroute.Splash.route)  {
        composable(navroute.Splash.route) {
            SplashScreen(navController)
        }
        composable(navroute.Login.route) {
            LoginScreen(navController)
        }
        composable(navroute.Register.route) {
            RegisterScreen(navController)
        }
        composable(navroute.Dashboard.route) {
            DashboardScreen(navController)
        }
        composable(navroute.Profile.route) {
            ProfileScreen(navController)
        }
        composable(navroute.notification.route) {
            NotificationsScreen(navController)
        }
        composable(navroute.language.route) {
            LanguageScreen(navController)
        }

    }

}

