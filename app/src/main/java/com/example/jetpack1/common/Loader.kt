package com.example.jetpack1.common

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun BudgetLoaderScreen() {
    // 1. Currencies list to cycle through
    val currencies = remember { listOf("₹", "$", "€", "£", "¥") }
    var currentCurrencyIndex by remember { mutableStateOf(0) }

    // 2. Infinite transition for the rotating outer circle
    val infiniteTransition = rememberInfiniteTransition(label = "LoaderTransition")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RotationAngle"
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentCurrencyIndex = (currentCurrencyIndex + 1) % currencies.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)), // Sleek Dark Background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Container for the Loader
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer Rotating Gradient Circle
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(rotationAngle)
                        .drawBehind {
                            // Budget Green to Emerald Gradient Arc
                            val gradientBrush = Brush.sweepGradient(
                                colors = listOf(
                                    Color(0xFF00EA96), // Bright Money Green
                                    Color(0xFF00A86B), // Deep Emerald
                                    Color(0xFF121212)  // Fades out into background
                                )
                            )
                            drawArc(
                                brush = gradientBrush,
                                startAngle = 0f,
                                sweepAngle = 280f, // Keeping a small gap for a premium look
                                useCenter = false,
                                style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                )

                // Inner Circle Background for the Symbol
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFF1E1E1E), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Smooth Crossfade animation when currency changes
                    Crossfade(
                        targetState = currencies[currentCurrencyIndex],
                        animationSpec = tween(durationMillis = 300),
                        label = "CurrencyCrossfade"
                    ) { currency ->
                        Text(
                            text = currency,
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}