package com.example.jetpack1.languagedatastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LanguageDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val LANGUAGE_KEY = stringPreferencesKey("language")
    }

    suspend fun saveLanguage(language: String) {
        Log.d("LanguageDataStore", "Saving language: $language") // Add this log
        context.languageDataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    val languageFlow: Flow<String> = context.languageDataStore.data
        .map { preferences ->
            val lang = preferences[LANGUAGE_KEY] ?: "en"
            Log.d("LanguageDataStore", "Loaded language: $lang") // Add this log
            lang
        }
}