@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.jetpack1.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack1.ui.theme.NegativeRed
import com.example.jetpack1.ui.theme.TextPrimary
import com.example.jetpack1.ui.theme.TextSecondary

@Composable
fun DashboardTopBar(
    title:String,
    scrollBehavior: TopAppBarScrollBehavior,
    userName: String = "Anubhav",
    isLarge: Boolean = false,
    onBackClick: (() -> Unit) = {},
    onMenuClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {

  if (isLarge) {
      LargeTopAppBar(
          scrollBehavior = scrollBehavior,
          colors = TopAppBarDefaults.largeTopAppBarColors(
              containerColor = MaterialTheme.colorScheme.surface,
              scrolledContainerColor = MaterialTheme.colorScheme.surface
          ),
          title = {
              val collapsedFraction = scrollBehavior.state.collapsedFraction

              Column {
                  if (collapsedFraction < 0.5f) {
                      Text(
                          text = "Hello, $userName 👋",
                          style = MaterialTheme.typography.labelMedium,
                          color = MaterialTheme.colorScheme.primary
                      )
                  }
                  Text(
                      text = title,
                      fontSize = 26.sp,
                      color = NegativeRed,
                      fontWeight = FontWeight.ExtraBold,
                  )



                  if (collapsedFraction < 0.5f) {
                      Text(
                          "BUDGET TRACKER",
                          color = Color(0xFF888899),
                          fontSize = 11.sp,
                          fontWeight = FontWeight.SemiBold,
                      )

                  }
              }
          },
          navigationIcon = {
              IconButton(onClick = onMenuClick) {
                  Icon(
                      imageVector = Icons.Default.Menu,
                      contentDescription = "Menu"
                  )
              }
          },
          actions = {
              IconButton(onClick = onNotificationClick) {
                  Icon(
                      imageVector = Icons.Default.Notifications,
                      contentDescription = "Notifications"
                  )
              }
          }
      )
  }else{
      TopAppBar(
          title = {
              Text(
                  text = title,
                  fontWeight = FontWeight.SemiBold
              )
          },
          navigationIcon = {
              IconButton(onClick = onBackClick) {
                  Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
              }
          }
      )
  }
}

@Preview
@Composable
private fun PreviewDashboardTopBar() {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    DashboardTopBar(
        title = "",
        scrollBehavior = scrollBehavior,
        userName = "Anubhav",
        isLarge = true )

}

