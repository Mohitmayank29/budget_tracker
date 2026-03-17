package com.example.jetpack1.screens.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpack1.common.CommonButton
import com.example.jetpack1.common.CommonOutlinedTextField
import com.example.jetpack1.navigation.navroute

@Composable
fun LoginScreen(navController: NavController,viewmodel: LoginViewmodel = hiltViewModel()) {


    val context = LocalContext.current
    var useremail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var state =  viewmodel.dataOrException.value


    Surface(Modifier
        .fillMaxSize()
        .background(Color.White)
        .navigationBarsPadding()
    ) {
        when {
            state.loading ->{
                CircularProgressIndicator()
            }
            state.data != null ->{
                LaunchedEffect(Unit){
                    navController.navigate(navroute.Dashboard.route)
                }

            }
            else ->{
                state.e?.message
            }

        }
        Column(
            Modifier.fillMaxSize()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    Icons.Default.AccountCircle,
                    contentDescription = "Splash Screen Image",
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape)
                )
            }
            Column(Modifier
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    Modifier
                        .fillMaxWidth().padding(10.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Log-In",fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(Modifier.fillMaxWidth().padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CommonOutlinedTextField(
                            value = useremail,
                            onValueChange = { useremail = it },
                            label = "Username",
                            placeholder = "Enter Username",
                            modifier = Modifier.fillMaxWidth()

                        )
//                        Text("Password must be at least 8 characters long",
//                            color = Color.Red,
//                            textDecoration = TextDecoration.Underline,
//                            textAlign = TextAlign.Left)
                        CommonOutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Password",
                            placeholder = "Enter Password",
                            modifier = Modifier.fillMaxWidth(),
                            isPassword = true
                        )
                        CommonButton(text = "Login",
                            onClick = {
                                viewmodel.getlogin(useremail,password)
                            },

                        )
                        CommonButton(text = "Gooogle",
                            onClick = {
                                viewmodel.signinwithgoogle(context)
                            },

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
    LoginScreen(navController = NavController(LocalContext.current))
}
@Composable
fun RegisterScreen(navController: NavController)  {


}