package com.example.jetpack1.localmanager

import android.content.Context
import android.util.Log
import androidx.core.content.edit

object LanguagePreference {

    private const val PREF_NAME = "language_pref"
    private const val KEY_LANGUAGE = "language"

    fun saveLanguage(context: Context, language: String) {

        Log.d("LANGUAGE_PREF", "Saving Language: $language")

        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit { putString(KEY_LANGUAGE, language) }
    }

    fun getLanguage(context: Context): String {

        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val lang = pref.getString(KEY_LANGUAGE, "en") ?: "en"

        Log.d("LANGUAGE_PREF", "Current Language: $lang")

        return lang
    }
}