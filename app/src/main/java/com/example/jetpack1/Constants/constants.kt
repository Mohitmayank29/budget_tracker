package com.example.jetpack1.Constants

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack1.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow

object constants {

     val SomethingWentWrong = "Something Went Wrong"

    @Composable
    fun CustomToast(
        message: String,
        type: Int) {
        val icon = when (type) {
            0 -> R.drawable.ic_launcher_foreground
            1 -> R.drawable.ic_launcher_foreground
            2 -> R.drawable.ic_launcher_foreground
            else -> R.drawable.ic_launcher_foreground
        }
        Row( Modifier
            .padding(16.dp)
            .background(Color.Black, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
//
//    @Composable
//    fun AppSnackbarHost(
//        snackbarHostState: SnackbarHostState
//    ) {
//
//        // Collect Snackbar messages - FIXED spelling
//        LaunchedEffect(Unit) {
//            Log.d("SignupScreen", "Starting SnackbarManager collection")
//            SnackbarManager.snackbarFlow.collect { message ->  // Fixed: snackbarFlow (not snacshkbarFlow)
//                Log.d("SignupScreen", "📢 Showing snackbar message: $message")
//                snackbarHostState.showSnackbar(message)
//            }
//        }
//    }
}

    object SnackbarManager {
        private val _messages = Channel<String>()
        val messages = _messages.receiveAsFlow()

        suspend fun showMessage(message: String) {
            _messages.send(message)
        }
    }
