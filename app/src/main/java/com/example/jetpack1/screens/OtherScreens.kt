package com.example.jetpack1.screens

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.budget.tracker.data.Category
import com.budget.tracker.viewmodel.UiState
import com.example.jetpack1.screens.Dashboard.TransactionRow
import com.example.jetpack1.screens.Dashboard.formatCurrency
import com.example.jetpack1.ui.theme.Accent
import com.example.jetpack1.ui.theme.Background
import com.example.jetpack1.ui.theme.NegativeRed
import com.example.jetpack1.ui.theme.TextMuted
import com.example.jetpack1.ui.theme.TextPrimary
import com.example.jetpack1.ui.theme.TextSecondary
import java.time.format.DateTimeFormatter

// --- History Screen ---

@Composable
fun HistoryScreen(
    state: UiState,
    onBack: () -> Unit,
    onDeleteTransaction: (Long) -> Unit
) {
    val monthLabel = state.selectedMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp, start = 16.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text("History", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                Text("$monthLabel · ${state.transactions.size} transactions", color = TextMuted, fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(16.dp))

        if (state.transactions.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No transactions this month.", color = TextMuted)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(state.transactions) { tx ->
                    TransactionRow(transaction = tx, onDelete = { onDeleteTransaction(tx.id) })
                }
            }
        }
    }
}

// --- Budget Screen ---

@Composable
fun BudgetScreen(
    state: UiState,
    onBack: () -> Unit,
    onSetBudget: (Category, Double) -> Unit
) {
    var editingCategory by remember { mutableStateOf<Category?>(null) }
    val monthLabel = state.selectedMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp, start = 16.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text("Budgets", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                Text(monthLabel, color = TextMuted, fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 40.dp)
        ) {
            Category.entries.forEach { cat ->
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
                            Text(cat.label, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(0.06f))
                                .clickable { editingCategory = cat }
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
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${formatCurrency(spent)} spent", color = if (over) NegativeRed else TextSecondary, fontSize = 12.sp)
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
        }
    }

    // Budget edit dialog
    editingCategory?.let { cat ->
        BudgetEditDialog(
            category = cat,
            currentBudget = state.budgets.find { it.category == cat }?.amount ?: 0.0,
            onConfirm = { amount ->
                onSetBudget(cat, amount)
                editingCategory = null
            },
            onDismiss = { editingCategory = null }
        )
    }
}

@Composable
fun BudgetEditDialog(
    category: Category,
    currentBudget: Double,
    onConfirm: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    var input by remember { mutableStateOf(if (currentBudget > 0) currentBudget.toInt().toString() else "") }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF1A1A26))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("${category.emoji} ${category.label}", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Text("Set monthly budget", color = TextMuted, fontSize = 13.sp)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Amount (₹)", color = TextMuted) },
                textStyle = LocalTextStyle.current.copy(color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = outlinedTextFieldColors(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
                ) { Text("Cancel") }
                Button(
                    onClick = { onConfirm(input.toDoubleOrNull() ?: 0.0) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Accent)
                ) { Text("Save", fontWeight = FontWeight.Bold) }
            }
        }
    }
}
