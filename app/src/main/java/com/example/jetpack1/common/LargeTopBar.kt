@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.jetpack1.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack1.ui.theme.NegativeRed

@Composable
fun DashboardTopBar(
    title:String,
    scrollBehavior: TopAppBarScrollBehavior,
    userName: String = "Anubhav",
    onMenuClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
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
}

@Composable
fun CommonTopbar(
    title: String,
    onbackclick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                color = NegativeRed,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onbackclick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        }
    )
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
        )

}
@Preview
@Composable
fun BottomNavigationBar() {
// val startDestination = ComposeNavigator.Destination.
//    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationBarItem(
                selected = true,
                onClick = { },
                icon = {
                    Icon(Icons.Default.Home, contentDescription = "Home")
                },
                label = { Text("Home") },
                modifier = Modifier.size(30.dp).padding(vertical = 4.dp)
            )
            NavigationBarItem(
                selected = false,
                onClick = { },
                icon = {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                },
                label = { Text("Profile") },
                modifier = Modifier.size(30.dp).padding(vertical = 4.dp)
            )
            NavigationBarItem(
                selected = false,
                onClick = { },
                icon = {
                    Icon(Icons.Default.Refresh, contentDescription = "Settings")
                },
                label = { Text("Settings") },
                modifier = Modifier.size(30.dp).padding(vertical = 4.dp)
            )
        }
    }
}
