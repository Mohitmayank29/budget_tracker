package com.example.jetpack1.screens.Login.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpack1.Constants.constants
import com.example.jetpack1.R
import com.example.jetpack1.common.CommonButton
import com.example.jetpack1.common.CommonOutlinedTextField
import com.example.jetpack1.common.SnackbarController
import com.example.jetpack1.data.ApiResult
import com.example.jetpack1.datastore.PreferencesEncryptedShared
import com.example.jetpack1.navigation.navroute
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun     RegisterScreen(navController: NavController,viewModel: SignUpViewModel = hiltViewModel()) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(state) {
        Log.d("SignupScreen", "State changed to: $state")
        when (state) {
            is ApiResult.Loading -> {
                Log.d("SignupScreen", "⏳ Loading state detected")
            }
            is ApiResult.Success -> {
                Log.d("SignupScreen", "✅ Success state detected! Email: $email")
                viewModel.setPreferenceEncryptedShared(PreferencesEncryptedShared.commonemail,email)
                SnackbarController.manager.success("Account created successfully! Please login.")
                delay(1500)
                navController.navigate("${navroute.Login.route}?email=$email") {
                    popUpTo(navroute.signup.route) { inclusive = true }
                }
            }
            is ApiResult.Error -> {
                val errorMsg = (state as ApiResult.Error).message
                Log.e("SignupScreen", "❌ Error state detected: $errorMsg")
                SnackbarController.manager.error(errorMsg)
            }
            else -> {
                Log.d("SignupScreen", "State is null or unknown")
            }
        }
    }
    if (state is ApiResult.Loading) {
        Log.d("SignupScreen", "Showing loading indicator")

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF0D1B2A),
                        Color(0xFF1B263B),
                        Color(0xFF0A0F1C)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // 🔥 LOGO
            Image(
                painter = painterResource(R.drawable.logo1),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 20.dp)
            )

            // 🔥 CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1B263B)
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        "Sign Up",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )

//                    CommonOutlinedTextField(
//                        value = name,
//                        onValueChange = { name = it },
//                        label = "Name",
//                        placeholder = "Enter name"
//                    )

                    CommonOutlinedTextField(
                        value = email,
                        onValueChange = { email = it
                            Log.d("SignupScreen", "Email changed to: $it")
                        },
                        label = "Email",
                        placeholder = "Enter email",
                        keyboardType = KeyboardType.Email
                    )

                    CommonOutlinedTextField(
                        value = password,
                        onValueChange = { password = it
                            Log.d("SignupScreen", "Password length: ${it.length}")
                        },
                        label = "Password",
                        placeholder = "Enter password",
                        isPassword = true
                    )

                    CommonButton(
                        text = "Create Account",
                        onClick = {
                            Log.d("SignupScreen", "📝 Create Account clicked")
                            Log.d("SignupScreen", "Email: $email")
                            Log.d("SignupScreen", "Password length: ${password.length}")
                            when {
                                email.isBlank() -> {
                                    Log.w("SignupScreen", "Validation failed: Email is blank")
                                     SnackbarController.manager.error("Please enter email")
                                }

                                password.length < 6 -> {
                                    Log.w("SignupScreen", "Validation failed: Password too short")
                                      SnackbarController.manager.error("Password must be at least 6 characters")
                                }
                                else -> {
                                    Log.d("SignupScreen", "✅ Validation passed, calling viewModel.getsignup")
                                    viewModel.getsignup(email, password)
                                }
                            }
                        }
                    )

                    Text(
                        text = "Already have an account? Login",
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewRegisterScreen() {
    RegisterScreen(navController = NavController(LocalContext.current))
}