package com.example.jetpack1.screens.customize

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpack1.Database.Table.CategoryTable
import com.example.jetpack1.R
import com.example.jetpack1.common.BudgetLoaderScreen
import com.example.jetpack1.common.CommonOutlinedTextField
import com.example.jetpack1.common.DashboardTopBar
import com.example.jetpack1.common.SnackbarController
import com.example.jetpack1.data.ApiResult
import com.example.jetpack1.enumclasses.TopBarType
import com.example.jetpack1.enumclasses.TransactionType
import com.example.jetpack1.ui.theme.Background
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.jetpack1.Database.Table.TransactionTable
import com.example.jetpack1.enumclasses.Category
import com.example.jetpack1.screens.Dashboard.formatCurrency
import com.example.jetpack1.ui.theme.CardBackground
import com.example.jetpack1.ui.theme.NegativeRed
import com.example.jetpack1.ui.theme.PositiveGreen
import com.example.jetpack1.ui.theme.TextMuted
import com.example.jetpack1.ui.theme.TextPrimary
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizeScreen(navController: NavController,viewModel: CustomizeViewModel = hiltViewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    var categoryName by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("") }
    val result = viewModel.state.collectAsState()
    val state = result.value

    LaunchedEffect(state) {
        when (state) {


            is ApiResult.Error<*> -> {
                SnackbarController.manager.error(state.message)

            }
            else -> {}

        }
    }
    if(state is ApiResult.Loading){
        BudgetLoaderScreen()
    }
    if (showDialog) {

        AlertDialog(

            onDismissRequest = {
                showDialog = false
            },

            title = {
                Text("Add Category")
            },

            text = {

                Column {

                    CommonOutlinedTextField(
                        value = categoryName,
                        onValueChange = {
                            categoryName = it
                        },
                        label = "Category Name",

                        placeholder = "Category Name"

                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    CommonOutlinedTextField(
                        value = emoji,
                        onValueChange = {
                            emoji = it
                        },
                        label = "Emoji",
                        placeholder = "Add Emoji"
                    )
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        viewModel.insertcategory(
                            CategoryTable(
                                label = categoryName,
                                emoji = emoji,
                                colorHex = 0xFF2196F3,
                                type = TransactionType.EXPENSE
                            )
                        )
                        showDialog = false
                        categoryName = ""
                        emoji = ""
                    }
                ) {

                    Text("Save")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {

                    Text("Cancel")
                }
            }
        )
    }

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

        FloatingActionButton(
            onClick = {
                showDialog = true
            }

        ) {

            Text("+")
        }

      if(  state is ApiResult.Success) {
          val resultdata = state.data ?: emptyList()

          LazyColumn(
              Modifier
                  .fillMaxSize()
                  .padding(10.dp)
          ) {
              items(resultdata.size) { index ->
                  categoryrow(
                      category = resultdata[index]
                  )
              }
          }
      }
    }
}
@Composable
fun categoryrow(category: CategoryTable) {

    val viewModel: CustomizeViewModel = hiltViewModel()

    var showDelete by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .clickable {
                showDelete = !showDelete
            }
            .padding(14.dp),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(category.colorHex).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = category.emoji,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.padding(6.dp))

            Column {

                Text(
                    text = category.label,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = category.type.name,
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }

        if (showDelete) {

            IconButton(
                onClick = {

                    viewModel.deletecategory(category.id)
                }
            ) {

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = NegativeRed
                )
            }
        }
    }
}

