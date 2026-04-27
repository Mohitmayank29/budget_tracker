package com.example.jetpack1.screens.Login.login

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpack1.R
import com.example.jetpack1.common.CommonButton
import com.example.jetpack1.common.CommonOutlinedTextField
import com.example.jetpack1.navigation.navroute
import com.example.jetpack1.screens.Login.loginsignupScreen.LoginSignupViewmodel

@SuppressLint("ContextCastToActivity")
@Composable
fun LoginScreen(
    navController: NavController,
    viewmodel: LoginSignupViewmodel = hiltViewModel()
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                        "Login",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )

                    CommonOutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        placeholder = "Enter email",
                        leadingIcon = Icons.Default.AccountCircle
                    )

                    CommonOutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        placeholder = "Enter password",
                        isPassword = true,
                        leadingIcon = Icons.Default.Lock
                    )

                    CommonButton(
                        text = "Login",
                        onClick = {
                            viewmodel.getlogin(email, password)
                        }
                    )

                    Text(
                        text = "Don't have an account? Sign Up",
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                navController.navigate(navroute.signup.route)
                            }
                    )
                }
            }
        }
    }
}