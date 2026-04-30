package com.example.jetpack1.screens.Login.login

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.data.ApiResult
import com.example.jetpack1.datastore.PreferencesDataStore
import com.example.jetpack1.datastore.PreferencesEncryptedShared
import com.example.jetpack1.datastore.PreferencesEncryptedShared.securePrefs
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth : FirebaseAuth,
    private val preferencesDataStore: PreferencesDataStore,
) : ViewModel(){
    private val _state = MutableStateFlow<ApiResult<AuthResult>?>(null)
    val  state : StateFlow<ApiResult<AuthResult>?> = _state
    @SuppressLint("SuspiciousIndentation")
    suspend fun getPreferencesData(key: String): String {
        return preferencesDataStore.getPreferenceDataStore(key).first()
    }
    fun getPreferenceEncryptedShared(key: String): String? {
        return securePrefs?.getString(key, null)
    }
    fun getlogin(email:String, password:String){
        viewModelScope.launch {
            try {
                _state.value = ApiResult.Loading()
                val result = auth.signInWithEmailAndPassword(email, password).await()
                _state.value = ApiResult.Success(result)
            } catch (ex: Exception){
                _state.value = ApiResult.Error(
                    ex.message ?: "error"
                )
            }
        }
    }
}
