package com.example.jetpack1.screens.profileScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ButtonDefaults

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jetpack1.R
import com.example.jetpack1.common.DashboardTopBar
import com.example.jetpack1.enumclasses.TopBarType
import com.example.jetpack1.navigation.navroute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }
    var darkMode by remember {
        mutableStateOf(false)
    }
    var Notifi by remember {
        mutableStateOf(false)
    }
    Column(
        Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(Color.White)


    ) {
        DashboardTopBar(
            title = "Profile",
            type = TopBarType.BACK_ONLY,
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            onbackclick = {
                navController.popBackStack()
            }
        )
        Column(
            Modifier.fillMaxSize()
                    .padding(10.dp)
        ) {
            LazyColumn(
                Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    UserProfileCard()
                }
                item {
                    SettingsSection(
                        isDarkMode = darkMode,
                        onDarkModeChange = {
                            darkMode = it
                        },
                        isNotifMode = Notifi,
                        onNotificationClick = {
                            Notifi = it
                        },
                        onCategoryClick = {
                            navController.navigate(navroute.customize.route)
                        },
                        onLanguageClick = {
                            navController.navigate(navroute.language.route)
                        },
                        onPrivacyClick = {},
                        onLogoutClick = {
                            showLogoutDialog= true
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

            }

        }
    }

    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = {
                showLogoutDialog = false
            },
            onLogout = {
                showLogoutDialog = false

                navController.navigate(navroute.loginsignup.route) {
                    popUpTo(navroute.Profile.route) { inclusive = true }
                }
            }
        )
    }
}
@Preview
@Composable
private fun Previewcard() {
    UserProfileCard()
}
@Composable
fun UserProfileCard(
    name: String = "Mohit Kumar",
    email: String = "mohit@gmail.com",
    phone: String = "+91 9876543210",
    onUploadPhoto: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
        .padding(bottom = 10.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                contentAlignment = Alignment.BottomEnd
            ) {

                Image(
                    painter = painterResource(R.drawable.account),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .border(
                            3.dp,
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        ),
                    contentScale = ContentScale.Crop
                )

                FloatingActionButton(
                    onClick = onUploadPhoto,
                    modifier = Modifier.size(38.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = "Upload",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProfileItem(
                icon = Icons.Default.Email,
                title = "Email",
                value = email
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileItem(
                icon = Icons.Default.Phone,
                title = "Phone",
                value = phone
            )
        }
    }
}

@Composable
fun ProfileItem(
    icon: ImageVector,
    title: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(size = 14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
@Composable
fun SettingsSection(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    isNotifMode: Boolean,
    onNotificationClick: (Boolean) -> Unit,
    onCategoryClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {

        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {

            SettingSwitchItem(
                icon = Icons.Default.DarkMode,
                title = "Dark Mode",
                checked = isDarkMode,
                onCheckedChange = onDarkModeChange
            )

            Divider()

            SettingSwitchItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                checked = isNotifMode,
                onCheckedChange = onNotificationClick
            )

            Divider()

            SettingItem(
                icon = Icons.Default.Category,
                title = "Customize Categories",
                onClick = onCategoryClick
            )

            Divider()

            SettingItem(
                icon = Icons.Default.Language,
                title = "Language",
                onClick = onLanguageClick
            )

            Divider()

            SettingItem(
                icon = Icons.Default.Policy,
                title = "Privacy Policy",
                onClick = onPrivacyClick
            )

            Divider()

            SettingItem(
                icon = Icons.AutoMirrored.Filled.Logout,
                title = "Logout",
                titleColor = Color.Red,
                iconColor = Color.Red,
                onClick = onLogoutClick
            )
        }
    }
}
@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = titleColor,
            fontSize = 16.sp
        )

        Icon(
            Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}
@Composable
fun SettingSwitchItem(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onLogout: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,

        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp)
            )
        },

        title = {
            Text(
                text = "Logout",
                style = MaterialTheme.typography.titleLarge
            )
        },

        text = {
            Text(
                text = "Are you sure you want to logout from your account?"
            )
        },

        dismissButton = {
            OutlinedButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        },

        confirmButton = {
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Logout")
            }
        }
    )
}

@Preview
@Composable
private fun previewprofilre() {
    val navController = rememberNavController()
    ProfileScreen(navController = navController)
}
    
