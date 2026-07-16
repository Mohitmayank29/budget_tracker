package com.example.jetpack1

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import com.example.jetpack1.Constants.constants
import com.example.jetpack1.navigation.NavigationScreen
import com.example.jetpack1.screens.language.LocaleHelper
import com.example.jetpack1.ui.theme.Jetpack1Theme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class  MainActivity : ComponentActivity() {
    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val prefs = getSharedPreferences("language_pref", Context.MODE_PRIVATE)
        val language = prefs.getString(constants.savedLanguage, "en") ?: "en"
        setContent {
            val configuration = LocalConfiguration.current
            val updatedConfiguration = Configuration(configuration).apply {
                setLocale(Locale(language))
            }
            Jetpack1Theme {
                CompositionLocalProvider(
                    LocalConfiguration provides updatedConfiguration
                ) {
                    Surface {
                        NavigationScreen()
                    }
                }
            }
        }
    }
    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences(
            "language_pref",
            Context.MODE_PRIVATE
        )

        val language = prefs.getString(constants.savedLanguage, "en") ?: "en"
        Log.d("LANGUAGE", "Loaded: $language")
        super.attachBaseContext(
            LocaleHelper.setLocale(
                newBase,
                language
            )
        )
    }
}

