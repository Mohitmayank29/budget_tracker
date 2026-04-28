package com.example.jetpack1.screens.Login.loginsignupScreen

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.R
import com.example.jetpack1.data.ApiResult
import com.example.jetpack1.datastore.PreferencesDataStore
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginSignupViewmodel  @Inject constructor(
    private val auth : FirebaseAuth,
    private val preferencesDataStore: PreferencesDataStore
): ViewModel() {

    private val _state = MutableStateFlow<ApiResult<AuthResult>?>(null)
    val  state : StateFlow<ApiResult<AuthResult>?> = _state
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

        val credentialManager = CredentialManager.Companion.create(activity)

        viewModelScope.launch {
            try {
                _state.value = ApiResult.Loading()

                    Log.d("LOGIN_DEBUG", "Before getCredential")

                    val result = credentialManager.getCredential(activity, request)
                    val credential = result.credential
                    Log.d("LOGIN_DEBUG", "After getCredential")



                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {

                    val googleCredential =
                        GoogleIdTokenCredential
                            .createFrom(credential.data)

                    val idToken = googleCredential.idToken

                    firebaseAuthWithGoogle(idToken)
                }else {
                    _state.value = ApiResult.Error("Invalid credential type")
                }

            } catch (e: Exception) {
                Log.e("LOGIN_DEBUG", "Google Sign-In Error: ${e.message}", e)
                _state.value = ApiResult.Error(e.message ?: "Some Error")
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

    viewModelScope.launch {
        try {
            val result = auth.signInWithCredential(credential).await()
            // Get fresh token
            val user = result.user
            user?.getIdToken(true)?.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val token = task.result?.token
                    Log.d("TAG", "Token: $token")

                    viewModelScope.launch {
                        preferencesDataStore.setPreferenceDataStore(
                            PreferencesDataStore.usergeneratedtoekn,
                            token ?: ""
                        )
                    }
                }
            }
           _state.value = ApiResult.Success(result)

        } catch (e: Exception) {
            Log.d("TAG", "Error: ${e.message}")
            Log.e("TAG", "Firebase Auth Error: ${e.message}", e)
            _state.value = ApiResult.Error(e.message ?: "Firebase Authentication Failed")
        }
    }
}
//    @SuppressLint("SuspiciousIndentation")
//    fun getlogin(email:String, password:String){
//        try{
//        _state.value = ApiResult.Loading()
//             viewModelScope.launch {
//                 val result = auth.signInWithEmailAndPassword(email,password).await()
//
//
//                 _state.value = ApiResult.Success(result)
//
//
//
//             }
//        }catch (ex: Exception){
//           _state.value = ApiResult.Error(
//               ex.message ?: "error"
//           )
//        }
//
//    }

}