package com.example.jetpack1.screens.Login.loginsignupScreen

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpack1.R
import com.example.jetpack1.navigation.navroute
import com.example.jetpack1.screens.Login.loginsignupScreen.LoginSignupViewmodel

@Composable
fun LoginSignUpScreen(
    navController: NavController,
    viewmodel: LoginSignupViewmodel = hiltViewModel()
) {

    val activity = LocalContext.current as Activity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0D1B2A),
                        Color(0xFF1B263B),
                        Color(0xFF0A0F1C)
                    )
                )
            )
            .navigationBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {


            Image(
                painter = painterResource(id = R.drawable.logo1),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 10.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Button(
                    onClick = {
                        navController.navigate(navroute.Login.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD700),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                // SIGN UP BUTTON
                OutlinedButton(
                    onClick = {
                        navController.navigate(navroute.signup.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(30.dp),
                    border = BorderStroke(1.dp, Color(0xFFFFD700))
                ) {
                    Text(
                        "Sign Up",
                        color = Color(0xFFFFD700),
                        fontSize = 18.sp
                    )
                }
            }

            // 🔥 BOTTOM SOCIAL LOGIN
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 30.dp)
            ) {

                Text(
                    text = "-- OR CONTINUE WITH --",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    IconButton(
                        onClick = {
                            viewmodel.signinwithgoogle(activity)
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.White, CircleShape)

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }

                    // FACEBOOK
                    IconButton(
                        onClick = {
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color(0xFF1877F2), CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.facebook),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
@Preview
@Composable
private fun PreviewLoginScreen() {
    LoginSignUpScreen(navController = NavController(LocalContext.current))
}


