package com.example.jetpack1.navigation

sealed class navroute(val route : String) {
       object Splash : navroute("splash")
       object Login : navroute("login")
       object Register : navroute("register")
       object Dashboard : navroute("dashboard")
       object Profile : navroute("profile")
       object Settings : navroute("settings")
       object Help : navroute("help")
       object About : navroute("about")
       object Contact : navroute("contact")
       object Feedback : navroute("feedback")
       object notification : navroute("notification")
       object language : navroute("laguageScreen")


}