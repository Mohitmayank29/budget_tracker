@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.jetpack1.common
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jetpack1.R
import com.example.jetpack1.enumclasses.TopBarType
import com.example.jetpack1.navigation.navroute
import com.example.jetpack1.ui.theme.NegativeRed
import com.example.jetpack1.ui.theme.Surface1
import java.time.format.DateTimeFormatter

@Composable
fun DashboardTopBar(
    title:String,
    scrollBehavior: TopAppBarScrollBehavior,
    type: TopBarType = TopBarType.BACK_ONLY,
    userName: String = "Anubhav",
    onMenuClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onAccountClick: () -> Unit = {},
    onbackclick: () -> Unit = {},
) {
//    val monthLabel = state.selectedMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
    when (type) {
        TopBarType.LARGE -> {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.background,
                                Surface1
                            )
                        )
                    )
            ) {
                LargeTopAppBar(
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color.Transparent/*MaterialTheme.colorScheme.surface*/,
                        scrolledContainerColor = Color.Transparent
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
                                painter = painterResource(R.drawable.notification),
                                contentDescription = "notification",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        IconButton(onClick = onAccountClick) {
                            Icon(
                                painter = painterResource(R.drawable.account),
                                contentDescription = "Account",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                )
            }
        }
        TopBarType.SMALL -> {
            CenterAlignedTopAppBar(

                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.SemiBold,
                            color = NegativeRed,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
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
                            painter = painterResource(R.drawable.notification),
                            contentDescription = "notification",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    IconButton(onClick = onAccountClick) {
                        Icon(
                            painter = painterResource(R.drawable.account),
                            contentDescription = "Accounts",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
        }
        TopBarType.BACK_ONLY -> {
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
                },
            )
        }
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
        )

}
@Composable
fun BottomNavigationBar(navController: NavController) {

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route
    NavigationBar(
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
            .windowInsetsPadding(WindowInsets.navigationBars),
        windowInsets = WindowInsets(0),

    ) {
        NavigationBarItem(
            selected = currentRoute == navroute.home.route ,
            onClick = {  navController.navigate(navroute.home.route) {
                popUpTo(navroute.home.route)
                launchSingleTop = true
            } },
            icon = {
                Icon(painter = painterResource(R.drawable.homebutton), contentDescription = "Home", modifier = Modifier.size(25.dp),
                    tint = Color.Unspecified) },
            label = { Text("Home") },
//            modifier = Modifier.size(30.dp).padding(vertical = 4.dp)
            )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(navroute.AddTranscation.route){
                popUpTo(navroute.AddTranscation.route)
                launchSingleTop = true
            } },
            icon = {
                Icon(painter = painterResource(R.drawable.plus), contentDescription = "Settings", modifier = Modifier.size(50.dp),
                    tint = Color.Unspecified) },
//            label = { Text("Settings") },
        )
        NavigationBarItem(
            selected = currentRoute == navroute.history.route,
            onClick = {  navController.navigate(navroute.history.route) {
                popUpTo(navroute.history.route)
                launchSingleTop = true
            } },
            icon = {
                Icon(painter = painterResource(R.drawable.history), contentDescription = "history", modifier = Modifier.size(25.dp),
                    tint = Color.Unspecified) },
            label = { Text("history") },
//            modifier = Modifier.size(30.dp).padding(vertical = 4.dp)
            )

    }
}

