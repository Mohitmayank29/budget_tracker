package com.example.jetpack1.screens.Login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.getString
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.R
import com.example.jetpack1.data.DataOrException
import com.example.jetpack1.datastore.PreferencesDataStore
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
@HiltViewModel
class LoginViewmodel  @Inject constructor(
    private val auth : FirebaseAuth,
    private val preferencesDataStore: PreferencesDataStore
): ViewModel() {

    var dataOrException = mutableStateOf(
        DataOrException<AuthResult,Exception>()
    )
    private set
    fun signinwithgoogle(activity: Activity) {
        Log.d("LOGIN_DEBUG", "Function Called")
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(activity.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false) // IMPORTANT
            .setAutoSelectEnabled(false) // 🔥 ADD THIS
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(activity)

        viewModelScope.launch {
            try {
                dataOrException.value = dataOrException.value.copy(loading = true)

                    Log.d("LOGIN_DEBUG", "Before getCredential")

                    val result = credentialManager.getCredential(activity, request)
                    val credential = result.credential

                    Log.d("LOGIN_DEBUG", "After getCredential")



                if (credential is androidx.credentials.CustomCredential &&
                    credential.type == com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {

                    val googleCredential =
                        com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
                            .createFrom(credential.data)

                    val idToken = googleCredential.idToken

                    firebaseAuthWithGoogle(idToken)
                }

            } catch (e: Exception) {
                dataOrException.value = dataOrException.value.copy(
                    e = e,
                    loading = false
                )
            }
        }
    }
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
//
//        viewModelScope.launch {
//            try {
//                val result = auth.signInWithCredential(firebaseCredential).await()
//
//                dataOrException.value = dataOrException.value.copy(
//                    data = result,
//                    loading = false
//                )
//                Log.d("LOGIN_DEBUG", "Firebase Auth Success")
//                val userdata = auth.currentUser
//                Log.d("LOGIN_DEBUG", result.toString())
//                Log.d("LOGIN_DEBUG",userdata.toString())
//                Log.d("LOGIN_DEBUG", dataOrException.value.data.toString())
//
//            } catch (e: Exception) {
//                dataOrException.value = dataOrException.value.copy(
//                    e = e,
//                    loading = false
//                )
//            }
//        }
//    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
//                     task.result.user
                    Log.d("TAG", "${user?.displayName}")
                    Log.d("TAG", task.result.user.toString())
                    Log.d("sdfn", user?.providerData.toString())
                    Log.d("TAG", "${user?.email}")
                    Log.d("TAG", "${user?.phoneNumber}")
                    Log.d("TAG", "${user?.tenantId}")
                    Log.d("TAG", "${user?.getIdToken(true)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "Token: ${task.result.token}")
                        } else {
                            Log.d("TAG", "Error: ${task.exception}")
                        }
                    
                    }}")
                     val usergeneratedtoken  =user?.getIdToken(true)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "Token: ${task.result.token}")
                        } else {
                            Log.d("TAG", "Error: ${task.exception}")
                        }

                    }
                    viewModelScope.launch {
                        preferencesDataStore.setPreferenceDataStore(
                            PreferencesDataStore.usergeneratedtoekn,
                            usergeneratedtoken.toString()
                        )
                    }
                    val providers = user?.providerData
                    providers?.forEach { profile ->
                        Log.d("TAG", "Provider ID: ${profile.providerId}")
                        Log.d("TAG", "UID: ${profile.uid}")
                        Log.d("TAG", "Name: ${profile.displayName}")
                        Log.d("TAG", "Email: ${profile.email}")
                        Log.d("TAG", "Photo URL: ${profile.photoUrl}")
                        Log.d("TAG", "Photo URL: ${profile.phoneNumber}")
                    }

//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
//                    updateUI(null)
                }
            }
    }
    @SuppressLint("SuspiciousIndentation")
    fun getlogin(email:String, password:String){
        try{
        dataOrException.value = dataOrException.value.copy(loading = true)
             viewModelScope.launch {
                 val result = auth.signInWithEmailAndPassword(email,password).await()


                 dataOrException.value = dataOrException.value.copy(data = result)
                 dataOrException.value = dataOrException.value.copy(loading = false)


             }
        }catch (ex: Exception){
           dataOrException.value = dataOrException.value.copy(e = ex)
            dataOrException.value =dataOrException.value.copy(loading = false)
        }

    }

}