package com.example.jetpack1.screens.customize

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.jetpack1.common.DashboardTopBar
import com.example.jetpack1.enumclasses.TopBarType
import com.example.jetpack1.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizeScreen(navController: NavController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Background)
            .navigationBarsPadding()
    ) {
        DashboardTopBar(
            title = "Customize As You Want",
            type = TopBarType.BACK_ONLY,
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            onbackclick = {
                navController.popBackStack()
            }
        )



    }
}