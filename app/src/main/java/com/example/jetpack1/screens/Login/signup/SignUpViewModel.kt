package com.example.jetpack1.screens.Login.signup

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.data.ApiResult
import com.example.jetpack1.datastore.PreferencesDataStore
import com.google.android.gms.common.api.Api
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.printStackTrace

@SuppressLint("ContextCastToActivity")
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val preferencesDataStore: PreferencesDataStore,
) : ViewModel() {

    private val _state = MutableStateFlow<ApiResult<AuthResult>?>(null)
    val state: StateFlow<ApiResult<AuthResult>?> = _state
    fun validpassword(password: String) {
        try {
            viewModelScope.launch {

//                       auth.validatePassword(password)
                if (password.length < 6) {

                }
            }

        }catch (ex: Exception){
            ex.printStackTrace()
        }


    }

    fun getsignup( email: String, password: String) {
        viewModelScope.launch {
            try{
                _state.value = ApiResult.Loading()
                val result =   auth.createUserWithEmailAndPassword(email, password).await()
                             // Sign in success, update UI with the signed-in user's information
                             Log.d("TAG", "createUserWithEmail:success")
                             val user = auth.currentUser

//                    updateUI(user)
                _state.value = ApiResult.Success(result)
                viewModelScope.launch {
                    preferencesDataStore.setPreferenceDataStore(
                        PreferencesDataStore.signupemail,
                        email.toString()
                    )
                    preferencesDataStore.setPreferenceDataStore(
                        PreferencesDataStore.signuppassword,
                        password
                    )
                }
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
