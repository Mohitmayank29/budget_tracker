package com.example.jetpack1.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.budget.tracker.data.Category
import com.budget.tracker.data.TransactionType
import com.example.jetpack1.ui.theme.Accent
import com.example.jetpack1.ui.theme.AccentSecondary
import com.example.jetpack1.ui.theme.Background
import com.example.jetpack1.ui.theme.NegativeRed
import com.example.jetpack1.ui.theme.PositiveGreen
import com.example.jetpack1.ui.theme.TextMuted
import com.example.jetpack1.ui.theme.TextPrimary
import com.example.jetpack1.ui.theme.TextSecondary
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onBack: () -> Unit,
    onAdd: (Double, String, Category, TransactionType, LocalDate) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var label by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category.FOOD) }
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }
    var dateText by remember { mutableStateOf(LocalDate.now().toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 40.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp, start = 16.dp, end = 24.dp, bottom = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text(
                "Add Transaction",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(28.dp))

        // Type toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White.copy(0.06f))
                .padding(4.dp)
        ) {
            TransactionType.entries.forEach { t ->
                val isSelected = type == t
                val bgColor = when {
                    !isSelected -> Color.Transparent
                    t == TransactionType.EXPENSE -> NegativeRed
                    else -> PositiveGreen
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(bgColor)
                        .clickable { type = t }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (t == TransactionType.EXPENSE) "💸 Expense" else "💰 Income",
                        color = if (isSelected) Color.Black else TextMuted,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Amount
        InputLabel("Amount (₹)", modifier = Modifier.padding(horizontal = 24.dp))
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            placeholder = { Text("0", color = TextMuted, fontSize = 32.sp, fontWeight = FontWeight.Bold) },
            textStyle = LocalTextStyle.current.copy(
                color = TextPrimary, fontSize = 32.sp, fontWeight = FontWeight.Bold
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            colors = outlinedTextFieldColors(),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(20.dp))

        // Label
        InputLabel("Description", modifier = Modifier.padding(horizontal = 24.dp))
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = label,
            onValueChange = { label = it },
            placeholder = { Text("What was this for?", color = TextMuted) },
            textStyle = LocalTextStyle.current.copy(color = TextPrimary),
            singleLine = true,
            colors = outlinedTextFieldColors(),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(20.dp))

        // Date
        InputLabel("Date", modifier = Modifier.padding(horizontal = 24.dp))
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = dateText,
            onValueChange = { dateText = it },
            placeholder = { Text("YYYY-MM-DD", color = TextMuted) },
            textStyle = LocalTextStyle.current.copy(color = TextPrimary),
            singleLine = true,
            colors = outlinedTextFieldColors(),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        // Category (only for expenses)
        if (type == TransactionType.EXPENSE) {
            Spacer(Modifier.height(24.dp))
            InputLabel("Category", modifier = Modifier.padding(horizontal = 24.dp))
            Spacer(Modifier.height(12.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(Category.entries) { cat ->
                    val isSelected = selectedCategory == cat
                    val catColor = Color(cat.colorHex)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) catColor.copy(0.15f) else Color.White.copy(0.04f))
                            .border(
                                width = if (isSelected) 1.5.dp else 0.dp,
                                color = if (isSelected) catColor else Color.Transparent,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { selectedCategory = cat }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(cat.emoji, fontSize = 18.sp)
                        Text(
                            cat.label,
                            color = if (isSelected) catColor else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            maxLines = 2,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // Submit button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Brush.linearGradient(listOf(Accent, AccentSecondary)))
                .clickable {
                    val parsedAmount = amount.toDoubleOrNull() ?: return@clickable
                    val parsedDate = runCatching { LocalDate.parse(dateText) }.getOrDefault(LocalDate.now())
                    onAdd(parsedAmount, label, selectedCategory, type, parsedDate)
                },
            contentAlignment = Alignment.Center
        ) {
            Text("Add Transaction", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun InputLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text.uppercase(),
        color = TextSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.sp,
        modifier = modifier
    )
}

@Composable
fun outlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Accent,
    unfocusedBorderColor = Color.White.copy(0.1f),
    focusedContainerColor = Color.White.copy(0.04f),
    unfocusedContainerColor = Color.White.copy(0.04f),
    cursorColor = Accent
)
