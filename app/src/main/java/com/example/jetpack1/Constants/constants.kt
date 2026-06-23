package com.example.jetpack1.Constants

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack1.R
object constants {

     val SomethingWentWrong = "Something Went Wrong"
     const val isuserlogin  = "isuserlogin"
    const val LANGUAGE_DIALOG_SHOWN = "language_dialog_shown"
    const val savedLanguage = "saved_language"

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
    fun getAppVersion(context: Context): Pair<String, Long> {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val versionName = packageInfo.versionName ?: "N/A"
        val versionCode =
            packageInfo.longVersionCode
        return versionName to versionCode
    }
}
