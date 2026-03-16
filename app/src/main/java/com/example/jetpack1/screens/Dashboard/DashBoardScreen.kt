package com.example.jetpack1.screens.Dashboard
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.items
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.budget.tracker.data.AppDatabase
import com.budget.tracker.data.BudgetRepository
import com.budget.tracker.data.Category
import com.budget.tracker.data.Transaction
import com.budget.tracker.data.TransactionType
import com.budget.tracker.viewmodel.BudgetViewModel
import com.budget.tracker.viewmodel.BudgetViewModelFactory
import com.budget.tracker.viewmodel.UiState
import com.example.jetpack1.R
import com.example.jetpack1.common.DashboardTopBar
import com.example.jetpack1.navigation.navroute
import com.example.jetpack1.ui.theme.Accent
import com.example.jetpack1.ui.theme.Background
import com.example.jetpack1.ui.theme.CardBackground
import com.example.jetpack1.ui.theme.NegativeRed
import com.example.jetpack1.ui.theme.PositiveGreen
import com.example.jetpack1.ui.theme.Surface1
import com.example.jetpack1.ui.theme.TextMuted
import com.example.jetpack1.ui.theme.TextPrimary
import com.example.jetpack1.ui.theme.TextSecondary
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale
import android.util.Log
import androidx.compose.ui.platform.LocalConfiguration


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController)  {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val database = remember {
        AppDatabase.getInstance(context)
    }
    val repository = remember {
        BudgetRepository(
            transactionDao = database.transactionDao(),
            budgetDao = database.budgetDao(),
            incomeDao = database.incomeDao()
        )
    }

    val factory = remember {
        BudgetViewModelFactory(repository)
    }

    val viewModel: BudgetViewModel = viewModel(factory = factory)
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            rememberTopAppBarState()
        )
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DashboardTopBar(
                title = stringResource(R.string.Budget),
                scrollBehavior = scrollBehavior,
                userName = "Mohit Kumar",
                onMenuClick = { navController.navigate(navroute.Profile.route) },
                onNotificationClick = { navController.navigate(navroute.language.route) },
            )
        }
    ){
        innerPadding ->
        HomeScreen(
            paddingValues = innerPadding,
            navController = navController,
            viewModel = viewModel
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(paddingValues: PaddingValues,navController: NavController,viewModel: BudgetViewModel) {
    val state by viewModel.uiState.collectAsState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .navigationBarsPadding(),
        contentPadding = PaddingValues(top = paddingValues.calculateTopPadding())
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(Surface1, Background))
                    )
            ) {
                // Month switcher
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}) {
                        Text("‹", color = TextSecondary, fontSize = 24.sp, fontWeight = FontWeight.Light)
                    }
                    Text(
                        state.selectedMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    IconButton(onClick = { }) {
                        Text("›", color = TextSecondary, fontSize = 24.sp, fontWeight = FontWeight.Light)
                    }
                }
            }
        }
        // Balance card
        item {
            BalanceCard(state = state, onSetIncomeClick = { })
        }
        // Category breakdown
        if (state.expensesByCategory.isNotEmpty()) {
            item {
                SectionTitle("By Category", modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp))
            }
            items(
                state.expensesByCategory.entries
                    .sortedByDescending { it.value }
                    .toList()
            ) { (category, amount) ->
                val budget = state.budgets.find { it.category == category }?.amount ?: 0.0
                CategoryRow(
                    category = category,
                    amount = amount,
                    budget = budget,
                    totalExpenses = state.expenses
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
        // Recent transactions
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle("Recent")
                if (state.transactions.size > 5) {
                    Text(
                        "See all",
                        color = Accent,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable {
//                            onSeeAllClick()
                        }
                    )
                }
            }
        }
        if (state.transactions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No transactions this month.\nTap + to add one.", color = TextMuted, fontSize = 14.sp)
                }
            }
        } else {
            items(state.transactions.take(6)) { tx ->
                TransactionRow(transaction = tx, onDelete = {
//                    onDeleteTransaction(tx.id)
                })
            }
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController) {

}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {

}

@Composable
fun HelpScreen(modifier: Modifier = Modifier) {

}

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {

}

@Composable
fun ContactScreen(modifier: Modifier = Modifier) {

}

@Composable
fun FeedbackScreen(modifier: Modifier = Modifier) {

}

@Composable
fun PrivacyPolicyScreen(modifier: Modifier = Modifier) {

}

@Composable
fun TermsAndConditionsScreen(modifier: Modifier = Modifier) {

}

@Composable
fun NotificationsScreen(navController: NavController) {

}

@Composable
fun SupportScreen(modifier: Modifier = Modifier) {

}

@Composable
fun FAQScreen(modifier: Modifier = Modifier) {

}

@Composable
fun PricingScreen(modifier: Modifier = Modifier) {

}

@Composable
fun BlogScreen(modifier: Modifier = Modifier) {

}

@Composable
fun GalleryScreen(modifier: Modifier = Modifier) {

}

@Composable
fun EventsScreen(modifier: Modifier = Modifier) {

}

@Composable
fun VideosScreen(modifier: Modifier = Modifier) {

}

@Composable
fun AudioScreen(modifier: Modifier = Modifier) {

}

@Composable
fun PodcastScreen(modifier: Modifier = Modifier) {

}

@Composable
fun LibraryScreen(modifier: Modifier = Modifier) {

}

@Composable
fun DownloadsScreen(modifier: Modifier = Modifier) {

}

@Composable
fun BookmarksScreen(modifier: Modifier = Modifier) {

}

@Composable
fun HistoryScreen(modifier: Modifier = Modifier) {

}

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {

}
@Composable
fun BalanceCard(state: UiState, onSetIncomeClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF1A1A2E), Color(0xFF16213E), Color(0xFF0F3460))
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Text(stringResource(R.string.hello), color = TextSecondary.copy(alpha = 0.7f), fontSize = 11.sp, letterSpacing = 1.sp)
            Text(
                formatCurrency(state.balance),
                color = if (state.balance >= 0) TextPrimary else NegativeRed,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Income
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.06f))
                        .padding(14.dp)
                ) {
                    Text("INCOME", color = TextSecondary.copy(0.5f), fontSize = 10.sp, letterSpacing = 1.sp)
                    Text(
                        formatCurrency(state.income),
                        color = PositiveGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        "+ Set income",
                        color = TextMuted,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable { onSetIncomeClick() }
                    )
                }
                // Spent
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.06f))
                        .padding(14.dp)
                ) {
                    Text("SPENT", color = TextSecondary.copy(0.5f), fontSize = 10.sp, letterSpacing = 1.sp)
                    Text(
                        formatCurrency(state.expenses),
                        color = NegativeRed,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    val count = state.transactions.count { it.type == TransactionType.EXPENSE }
                    Text("$count transactions", color = TextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}

@Composable
fun CategoryRow(
    category: Category,
    amount: Double,
    budget: Double,
    totalExpenses: Double
) {
    val catColor = Color(category.colorHex)
    val pct = if (budget > 0) (amount / budget).coerceIn(0.0, 1.0) else if (totalExpenses > 0) amount / totalExpenses else 0.0
    val overBudget = budget > 0 && amount > budget

    val animatedPct by animateFloatAsState(
        targetValue = pct.toFloat(),
        animationSpec = tween(600),
        label = "progress"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(category.emoji, fontSize = 20.sp)
                Text(category.label, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                if (overBudget) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NegativeRed.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("Over budget", color = NegativeRed, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(formatCurrency(amount), color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                if (budget > 0) Text("/ ${formatCurrency(budget)}", color = TextMuted, fontSize = 11.sp)
            }
        }
        Spacer(Modifier.height(10.dp))
        LinearProgressIndicator(
            progress = { animatedPct },
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = if (overBudget) NegativeRed else catColor,
            trackColor = Color.White.copy(alpha = 0.07f)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionRow(transaction: Transaction, onDelete: () -> Unit) {
    val catColor = Color(transaction.category.colorHex)
    var showDelete by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .clickable { showDelete = !showDelete }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(catColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(transaction.category.  emoji, fontSize = 18.sp)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                transaction.label,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                transaction.date.format(DateTimeFormatter.ofPattern("d MMM")),
                color = TextMuted,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Text(
            "${if (transaction.type == TransactionType.INCOME) "+" else "-"}${formatCurrency(transaction.amount)}",
            color = if (transaction.type == TransactionType.INCOME) PositiveGreen else NegativeRed,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (showDelete) {
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = NegativeRed, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text.uppercase(),
        color = TextSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.sp,
        modifier = modifier
    )
}

fun formatCurrency(amount: Double): String {
    val fmt = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    fmt.maximumFractionDigits = 0
    return fmt.format(amount)
}