package com.example.jetpack1.screens.language

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.Constants.constants
import com.example.jetpack1.datastore.PreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) : ViewModel() {

    suspend fun setPreferenceDataStore(key: String, value: String) {
        return preferencesDataStore.setPreferenceDataStore(key, value)
    }

    fun getPreferenceDataStore(key: String): Flow<String> {
        return preferencesDataStore.getPreferenceDataStore(key)
    }

    fun saveLanguage(context: Context, languageCode: String) {
        viewModelScope.launch {
            // Save to DataStore
            setPreferenceDataStore(constants.savedLanguage, languageCode)

            // Also save to SharedPreferences for attachBaseContext
            val prefs = context.getSharedPreferences(
                "language_pref",
                Context.MODE_PRIVATE
            )
            prefs.edit().putString("selected_language", languageCode).apply()
        }

        // Recreate activity after saving
        (context as? android.app.Activity)?.recreate()
    }
}