package com.example.jetpack1.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jetpack1.navigation.navroute
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    Surface(Modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        LaunchedEffect(Unit) {
            delay(1500)
            navController.navigate(navroute.Login.route)
        }
        Box(
            Modifier.fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
        ) {
            Image(
                Icons.Default.AccountCircle,
                contentDescription = "Splash Screen Image",
                modifier = Modifier.size(120.dp).clip(CircleShape)
            )
        }
    }
}