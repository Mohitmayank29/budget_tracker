package com.example.jetpack1.screens.AddBudget

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpack1.common.DashboardTopBar
import com.example.jetpack1.common.SnackbarController
import com.example.jetpack1.data.ApiResult
import com.example.jetpack1.enumclasses.Category
import com.example.jetpack1.enumclasses.TopBarType
import com.example.jetpack1.screens.BudgetEditDialog
import com.example.jetpack1.screens.Dashboard.UiState
import com.example.jetpack1.screens.Dashboard.formatCurrency
import com.example.jetpack1.ui.theme.Background
import com.example.jetpack1.ui.theme.NegativeRed
import com.example.jetpack1.ui.theme.TextMuted
import com.example.jetpack1.ui.theme.TextPrimary
import com.example.jetpack1.ui.theme.TextSecondary
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BudgetScreen(
    navController: NavController,
    viewModel: AddbudgetViewModel = hiltViewModel()
) {
    val result = viewModel.uiState.collectAsState()
    val state = result.value
    var editingCategory by remember { mutableStateOf<Category?>(null) }
    val monthLabel = state.selectedMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
    val totalBudget = state.budgets.sumOf { it.amount }
    val totalIncome = state.income
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .navigationBarsPadding()

    ) {
        DashboardTopBar(
            title = "Budgets",
            type = TopBarType.BACK_ONLY,
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            onbackclick = {
                navController.popBackStack()
            }
        )


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(bottom = 20.dp)
        ) {
            items(Category.entries) { cat ->
                setbudgetrow(
                    cat = cat,
                    state = state,
                    onEditClick = {editingCategory = it }
                )


            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(5.dp)
                    .background(Color.White.copy(0.04f)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Budget", color = TextMuted)
                Text(formatCurrency(totalBudget), color = TextPrimary)
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(5.dp)
                    .background(Color.White.copy(0.04f)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Income", color = TextMuted)
                Text(formatCurrency(totalIncome), color = TextPrimary)
            }
        }

    }

    editingCategory?.let { cat ->
        BudgetEditDialog(
            category = cat,
            currentBudget = state.budgets.find { it.category == cat }?.amount ?: 0.0,
            onConfirm = { amount ->
                    viewModel.setbudget(cat, amount)
                editingCategory = null
            },
            onDismiss = { editingCategory = null }
        )
    }
}

@Composable
fun setbudgetrow(
    cat: Category,
    state: UiState,
    onEditClick: (Category) -> Unit) {
    val budget = state.budgets.find { it.category == cat }?.amount ?: 0.0
    val spent = state.expensesByCategory[cat] ?: 0.0
    val pct = if (budget > 0) (spent / budget).coerceIn(0.0, 1.0) else 0.0
    val over = budget > 0 && spent > budget
    val catColor = Color(cat.colorHex)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(0.04f))
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(cat.emoji, fontSize = 20.sp)
                Text(
                    cat.label,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(0.06f))
                    .clickable { onEditClick(cat) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    if (budget > 0) formatCurrency(budget) else "+ Set",
                    color = if (budget > 0) TextPrimary else TextMuted,
                    fontSize = 13.sp
                )
            }
        }
        if (budget > 0) {
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { pct.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = if (over) NegativeRed else catColor,
                trackColor = Color.White.copy(0.07f)
            )
            Spacer(Modifier.height(6.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${formatCurrency(spent)} spent",
                    color = if (over) NegativeRed else TextSecondary,
                    fontSize = 12.sp
                )
                Text(
                    if (over) "${formatCurrency(spent - budget)} over" else "${
                        formatCurrency(
                            budget - spent
                        )
                    } left",
                    color = if (over) NegativeRed else TextMuted,
                    fontSize = 12.sp
                )
            }
        }
    }

}