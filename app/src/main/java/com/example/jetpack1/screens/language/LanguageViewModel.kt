package com.example.jetpack1.screens.language

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.Constants.constants
import com.example.jetpack1.datastore.PreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            setPreferenceDataStore(constants.savedLanguage, languageCode)
            Log.d("LANGUAGE", "Saving: $languageCode")
            // Also save to SharedPreferences for attachBaseContext
            val prefs = context.getSharedPreferences(
                "language_pref",
                Context.MODE_PRIVATE
            )
            prefs.edit().putString(constants.savedLanguage, languageCode).commit()
            (context as? Activity)?.recreate()

        }

    }
}