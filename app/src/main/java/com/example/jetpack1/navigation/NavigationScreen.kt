package com.example.jetpack1.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetpack1.language.LanguageScreen
import com.example.jetpack1.screens.Dashboard.DashboardScreen
import com.example.jetpack1.screens.Dashboard.NotificationsScreen
import com.example.jetpack1.screens.Dashboard.ProfileScreen
import com.example.jetpack1.screens.Login.loginsignupScreen.LoginSignUpScreen
import com.example.jetpack1.screens.Login.login.LoginScreen
import com.example.jetpack1.screens.Login.signup.RegisterScreen
import com.example.jetpack1.screens.PieChatScreen
import com.example.jetpack1.screens.splashScreen.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationScreen(modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = navroute.pie.route)  {
        composable(navroute.Splash.route) {
            SplashScreen(navController)
        }
        composable(navroute.loginsignup.route) {
            LoginSignUpScreen(navController)
        }
        composable( navroute.Login.route + "?email={email}",
            arguments = listOf(
                navArgument("email"){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )) { backStackEntry ->
            val emailarg = backStackEntry.arguments?.getString("email") ?: ""
            LoginScreen(navController,
                emailarg = emailarg)
        }
        composable(navroute.signup.route) {
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
        composable(navroute.pie.route) {
            PieChatScreen(navController)
        }

    }

}

