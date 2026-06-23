package com.example.jetpack1

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import com.example.jetpack1.Constants.constants
import com.example.jetpack1.navigation.NavigationScreen
import com.example.jetpack1.screens.language.LocaleHelper
import com.example.jetpack1.ui.theme.Jetpack1Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class  MainActivity : ComponentActivity() {
    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Jetpack1Theme {
                Surface {
                    NavigationScreen()
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

        super.attachBaseContext(
            LocaleHelper.setLocale(
                newBase,
                language
            )
        )
    }
}

