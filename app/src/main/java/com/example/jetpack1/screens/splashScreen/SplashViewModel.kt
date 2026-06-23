package com.example.jetpack1.screens.splashScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.Constants.constants
import com.example.jetpack1.datastore.PreferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val preferencesDataStore: PreferencesDataStore
) : ViewModel() {

    private val _loginstatus = mutableStateOf("")
    val loginstatus = _loginstatus

    suspend fun getPreferencesData(key: String): String {
        return preferencesDataStore.getPreferenceDataStore(key).first()
    }
    fun getPreferencestatus(key: String) {
        viewModelScope.launch {
            _loginstatus.value = getPreferencesData(key)
        }
    }
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun isLanguageDialogShown(): Boolean {
        return preferencesDataStore
            .getPreferenceDataStore(
                constants.LANGUAGE_DIALOG_SHOWN
            )
            .first()
            .toBoolean()
    }
}
