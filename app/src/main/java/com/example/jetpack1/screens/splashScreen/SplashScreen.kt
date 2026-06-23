package com.example.jetpack1.screens.splashScreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.jetpack1.R
import com.example.jetpack1.navigation.navroute
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SplashScreen(navController: NavController,splashViewModel: SplashViewModel = hiltViewModel()) {

    val logoScale = remember { Animatable(0.5f) }
    val logoAlpha = remember { Animatable(0f) }
    val textOffset = remember { Animatable(200f) }
    val lightOffset = remember { Animatable(500f) }


    LaunchedEffect(Unit) {

        // Light comes from right
        lightOffset.animateTo(
            targetValue = 0f,
            animationSpec = tween(800)
        )

        // Logo appears
        launch {
            logoAlpha.animateTo(1f, tween(800))
        }
        launch {
            logoScale.animateTo(1f, tween(800))
        }

        textOffset.animateTo(
            targetValue = 0f,
            animationSpec = tween(800)
        )
        val isloggedIn  =  splashViewModel.isUserLoggedIn()

        delay(800.milliseconds)
        if(isloggedIn) {
            navController.navigate(navroute.Dashboard.route) {
                popUpTo(navroute.Splash.route) { inclusive = true }
            }
        }else{
            navController.navigate(navroute.loginsignup.route){
                popUpTo(navroute.Splash.route){ inclusive = true}
            }
        }

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A)) // dark bg
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(200.dp)
                .offset(x = lightOffset.value.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )
        Image(
            painter = painterResource(id = R.drawable.logo1),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.Center)
                .scale(logoScale.value)
                .alpha(logoAlpha.value)
        )

    }
}
