package com.example.jetpack1.screens.Login

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.getString
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.R
import com.example.jetpack1.data.DataOrException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
@HiltViewModel
class LoginViewmodel  @Inject constructor(
    private val auth : FirebaseAuth): ViewModel() {

    var dataOrException = mutableStateOf(
        DataOrException<AuthResult,Exception>()
    )
    private set
    fun signinwithgoogle(context: Context) {

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false) // IMPORTANT
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(context)

        viewModelScope.launch {
            try {
                dataOrException.value = dataOrException.value.copy(loading = true)

                val result = credentialManager.getCredential(context, request)

                val credential = result.credential

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
    private fun firebaseAuthWithGoogle(idToken: String) {
        val firebaseCredential =
            com.google.firebase.auth.GoogleAuthProvider.getCredential(idToken, null)

        viewModelScope.launch {
            try {
                val result = auth.signInWithCredential(firebaseCredential).await()

                dataOrException.value = dataOrException.value.copy(
                    data = result,
                    loading = false
                )

            } catch (e: Exception) {
                dataOrException.value = dataOrException.value.copy(
                    e = e,
                    loading = false
                )
            }
        }
    }
    fun getlogin(email:String,password:String){
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