package com.example.jetpack1.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.navigation.NavController
import com.example.jetpack1.enumclasses.Category
import com.example.jetpack1.screens.Dashboard.TransactionRow
import com.example.jetpack1.screens.Dashboard.UiState
import com.example.jetpack1.screens.Dashboard.formatCurrency
import com.example.jetpack1.screens.addtranscation.outlinedTextFieldColors
import com.example.jetpack1.ui.theme.Accent
import com.example.jetpack1.ui.theme.Background
import com.example.jetpack1.ui.theme.NegativeRed
import com.example.jetpack1.ui.theme.TextMuted
import com.example.jetpack1.ui.theme.TextPrimary
import com.example.jetpack1.ui.theme.TextSecondary
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)


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
