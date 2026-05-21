package com.example.jetpack1.screens.addtranscation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import java.time.YearMonth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpack1.common.CommonButton
import com.example.jetpack1.common.CommonOutlinedTextField
import com.example.jetpack1.common.SnackbarController
import com.example.jetpack1.data.ApiResult
import com.example.jetpack1.enumclasses.Category
import com.example.jetpack1.enumclasses.TransactionType
import com.example.jetpack1.ui.theme.Accent
import com.example.jetpack1.ui.theme.NegativeRed
import com.example.jetpack1.ui.theme.PositiveGreen
import com.example.jetpack1.ui.theme.TextMuted
import com.example.jetpack1.ui.theme.TextSecondary
import java.time.LocalDate
import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import java.time.format.DateTimeFormatter
import java.util.Calendar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    navController: NavController,
    contentpadding: PaddingValues ,
    viewModel: AddTranscationViewModel = hiltViewModel()
//    onAdd: (Double, String, Category, TransactionType, LocalDate) -> Unit

) {
    var amount by remember { mutableStateOf("") }
    var label by remember { mutableStateOf("") }
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    var selectedCategory by remember { mutableStateOf(Category.FOOD) }
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }
    var dateText by remember { mutableStateOf(LocalDate.now().format(formatter)) }        // UI formatted
    var selectedDate by remember { mutableStateOf(LocalDate.now()) } // actual date
    val state = viewModel.state.collectAsState()
    val result = state.value
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->

            selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            dateText = selectedDate.format(formatter)

        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    LaunchedEffect(result) {
        when (result) {

            is ApiResult.Success<*> -> {
                val data = result.data
                SnackbarController.manager.success("Added in $type")
                Log.d("transcation1",data.toString())
                navController.popBackStack()
            }
            is ApiResult.Error<*> -> {
                val message = result.message
                SnackbarController.manager.error(message)
            }
            else -> {}
        }
    }
    if(result is ApiResult.Loading){
        CircularProgressIndicator()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(
            top = contentpadding.calculateTopPadding(),
            bottom = contentpadding.calculateBottomPadding()
        )
    ) {
        item {

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
                            .clickable {
                                if( type != t) {
                                    type = t
                                     amount = ""
                                     label = ""
                                     dateText = ""
                                     selectedDate = LocalDate.now()
                                     selectedCategory  = Category.FOOD


                                }
                            }
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
        }
        item {
            CommonOutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = "Amount",
                placeholder = "Enter Amount (₹)",
                keyboardType = KeyboardType.Number
            )
        }
        item {
            CommonOutlinedTextField(
                value = label,
                onValueChange = { label = it },
                label = "Description",
                placeholder = "What was this for?",
                singleLine = false,
            )
        }
        item {
            CommonOutlinedTextField(
                value = dateText,
                onValueChange = { dateText = it },
                label = "Date",
                placeholder = "Select From Calender",
                isCalender = true,
                onCalenderclick = {
                    datePickerDialog.show()
                }
            )
        }
        item {
            if (type == TransactionType.EXPENSE) {
                Spacer(Modifier.height(24.dp))
                InputLabel("Category")
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
                                .background(
                                    if (isSelected) catColor.copy(0.15f) else Color.White.copy(
                                        0.04f
                                    )
                                )
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
        }
        item {


             CommonButton(text = "Add Transaction", onClick = {
                 val parsedAmount = amount.toDoubleOrNull() ?: return@CommonButton
                 val parsedDate = selectedDate
                 val yearMonth = YearMonth.from(parsedDate)

                 viewModel.submitaddeddata(
                     amount = parsedAmount,
                     description = label,
                     date = parsedDate,
                     type =type,
                     category = if (type == TransactionType.EXPENSE) selectedCategory else null,
                     yearMonth =yearMonth
                 )
                 Log.d("data","$label ,$type $selectedCategory, $selectedDate,$parsedDate")
             })
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
