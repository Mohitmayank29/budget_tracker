package com.example.jetpack1.navigation

sealed class navroute(val route : String) {
       object Splash : navroute("splash")
       object Login : navroute("login")
       object signup : navroute("register")
       object loginsignup : navroute("loginsignup")
       object Dashboard : navroute("dashboard")
       object main : navroute("main")
       object home : navroute("DashBoard")
       object Profile : navroute("profile")
       object Settings : navroute("settings")
       object Help : navroute("help")
       object About : navroute("about")
       object Contact : navroute("contact")
       object Feedback : navroute("feedback")
       object notification : navroute("notification")
       object language : navroute("laguageScreen")
       object pie : navroute("pie")
       object history : navroute("History")
       object AddTranscation : navroute("AddTranscation")
       object addbudget : navroute("Add Budgets")
       object customize : navroute("Customize As You Want")
       object loader : navroute("Customize As You Want")


}