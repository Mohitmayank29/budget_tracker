package com.example.jetpack1

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.os.LocaleListCompat
import java.util.Locale
import androidx.lifecycle.lifecycleScope
import com.example.jetpack1.languagedatastore.LanguageDataStore
import com.example.jetpack1.navigation.NavigationScreen
import com.example.jetpack1.ui.theme.Jetpack1Theme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var languageDataStore: LanguageDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lifecycleScope.launch {
            languageDataStore = LanguageDataStore(this@MainActivity)
            languageDataStore.languageFlow.collect { language ->
                    val locale = LocaleListCompat.forLanguageTags(language)
                    AppCompatDelegate.setApplicationLocales(locale)
            }
        }
        setContent {
            Jetpack1Theme {
                // A surface container using the 'background' color from the theme
                Surface(Modifier.fillMaxSize().background(Color.White)) {
                    NavigationScreen()
                }
            }
        }
    }
}

