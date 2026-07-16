package com.example.jetpack1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


val Surface1 = Color(0xFF12121A)
val Surface2 = Color(0xFF1A1A26)
val CardBackground = Color(0x0AFFFFFF)
val Accent = Color(0xFF6366F1)
val AccentSecondary = Color(0xFF8B5CF6)
val PositiveGreen = Color(0xFF4ECDC4)
val NegativeRed = Color(0xFFFF6B6B)
val TextMuted = Color(0xFF444455)
val Primary = Color(0xFF16A34A)          // Green
val PrimaryContainer = Color(0xFFDCFCE7)

val Secondary = Color(0xFF0EA5E9)        // Blue

val Background = Color(0xFFF8FAFC)
val Surface = Color.White
val SurfaceVariant = Color(0xFFF1F5F9)

val Income = Color(0xFF22C55E)
val Expense = Color(0xFFEF4444)
val Savings = Color(0xFF2563EB)

val TextPrimary = Color(0xFF0F172A)
val TextSecondary = Color(0xFF64748B)
val DarkPrimary = Color(0xFF4ADE80)

val DarkBackground = Color(0xFF0F172A)
val DarkSurface = Color(0xFF1E293B)
val DarkSurfaceVariant = Color(0xFF334155)

val DarkIncome = Color(0xFF4ADE80)
val DarkExpense = Color(0xFFF87171)

val DarkText = Color.White
val DarkTextSecondary = Color(0xFFCBD5E1)
private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color.Black,

    secondary = Secondary,
    onSecondary = Color.Black,

    background = DarkBackground,
    onBackground = DarkText,

    surface = DarkSurface,
    onSurface = DarkText,

    surfaceVariant = DarkSurfaceVariant,

    error = DarkExpense
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,

    secondary = Secondary,
    onSecondary = Color.White,

    background = Background,
    onBackground = TextPrimary,

    surface = Surface,
    onSurface = TextPrimary,

    surfaceVariant = SurfaceVariant,

    error = Expense
)

@Composable
fun Jetpack1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
