package com.example.jetpack1.localmanager

import android.content.Context
import android.util.Log
import java.util.Locale

object LocaleManager {

    fun setLocale(context: Context, language: String): Context {

        Log.d("LANGUAGE_CHANGE", "Selected Language Code: $language")

        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}