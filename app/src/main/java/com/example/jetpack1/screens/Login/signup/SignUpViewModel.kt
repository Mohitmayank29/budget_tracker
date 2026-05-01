package com.example.jetpack1.screens.Login.signup

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.data.ApiResult
import com.example.jetpack1.datastore.PreferencesDataStore
import com.example.jetpack1.datastore.PreferencesEncryptedShared.securePrefs
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@SuppressLint("ContextCastToActivity")
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val preferencesDataStore: PreferencesDataStore,
) : ViewModel() {

    private val _state = MutableStateFlow<ApiResult<AuthResult>?>(null)
    val state: StateFlow<ApiResult<AuthResult>?> = _state
    fun setPreferenceEncryptedShared(key: String,value: String) {
        securePrefs?.edit()?.putString(key, value)?.apply()
    }
    fun validatePassword(password: String): String {
        return when {
            password.length < 6 -> "Password must be at least 6 digits"
            !password.all { it.isDigit() } -> "Password must contain only numbers"
            else -> "Valid"
        }
    }
    fun getsignup(email: String, password: String) {
        Log.d("password","$email, $password")
        val error = validatePassword(password)
        viewModelScope.launch {
            try{
                _state.value = ApiResult.Loading()
                val result =   auth.createUserWithEmailAndPassword(email, password).await()
                             // Sign in success, update UI with the signed-in user's information
                             Log.d("TAG", "createUserWithEmail:success")
                             val user = auth.currentUser
                _state.value = ApiResult.Success(result)
                    preferencesDataStore.setPreferenceDataStore(
                        PreferencesDataStore.signupemail,
                        email.toString()
                    )
                    preferencesDataStore.setPreferenceDataStore(
                        PreferencesDataStore.signuppassword,
                        password
                    )
//                    preferencesEncryptedSharedPreferences.setPreferenceEncryptedShared(
//                        PreferencesEncryptedShared.commonemail,
//                        email
//                    )

            } catch (ex: Exception){
                ex.printStackTrace()
                val errorMessage = when {
                    ex.message?.contains("email address is already in use") == true ->
                        "Email already registered. Please login."
                    ex.message?.contains("invalid email") == true ->
                        "Invalid email format"
                    else -> ex.message ?: "Signup failed. Please try again."
                }
                _state.value = ApiResult.Error(errorMessage)
                Log.e("SignUpViewModel", "Signup error: ${ex.message}")
            }
        }
    }
}
