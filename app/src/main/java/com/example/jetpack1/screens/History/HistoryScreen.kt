@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.jetpack1.screens.History

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jetpack1.screens.Dashboard.TransactionRow
import com.example.jetpack1.screens.Dashboard.UiState
import com.example.jetpack1.ui.theme.TextMuted
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    innerNavController: NavController,
    state: UiState,
    contentpadding : PaddingValues,
    onDeleteTransaction: (Int) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()

    ) {


//    Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 56.dp, start = 16.dp, end = 24.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(onClick = onBack) {
//                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
//            }
//            Column(modifier = Modifier.padding(start = 8.dp)) {
//                Text("History", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
//                Text("$monthLabel · ${state.transactions.size} transactions", color = TextMuted, fontSize = 13.sp)
//            }
//        }

        Spacer(Modifier.height(16.dp))

        if (state.transactions.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No transactions this month.", color = TextMuted)
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(
                    top = contentpadding.calculateTopPadding(),
                    bottom = contentpadding.calculateBottomPadding()
                )
            ) {
                items(state.transactions) { tx ->
                    TransactionRow(transaction = tx, onDelete = { onDeleteTransaction(tx.id) })
                }
            }
        }
    }
}