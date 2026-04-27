package com.example.jetpack1.screens.Login.signup

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.datastore.PreferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("ContextCastToActivity")
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val preferencesDataStore: PreferencesDataStore,
) : ViewModel() {


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

    fun getsignup(context: Context, email: String, password: String) {
         try {
             viewModelScope.launch {
                 auth.createUserWithEmailAndPassword(email, password)
                     .addOnCompleteListener { task ->
                         if (task.isSuccessful) {
                             // Sign in success, update UI with the signed-in user's information
                             Log.d("TAG", "createUserWithEmail:success")
                             val user = auth.currentUser
//                    updateUI(user)
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
                         } else {
                             // If sign in fails, display a message to the user.
                             Log.w("TAG", "createUserWithEmail:failure", task.exception)
                             Toast.makeText(
                                 context,
                                 "Authentication failed.",
                                 Toast.LENGTH_SHORT,
                             ).show()
//                    updateUI(null)
                         }
                     }
             }
         }catch (ex: Exception){
             ex.printStackTrace()
         }
    }
}