package com.example.jetpack1.screens.addtranscation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.Database.Table.CategoryTable
import com.example.jetpack1.Database.Table.TransactionTable
import com.example.jetpack1.data.ApiResult
import com.example.jetpack1.enumclasses.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class AddTranscationViewModel @Inject constructor(
    private val repository: AddTranscationRepository,
): ViewModel() {

   val _state = MutableStateFlow<ApiResult<TransactionTable>?>(null)
    val state : StateFlow<ApiResult<TransactionTable>?> =_state

    val _category = MutableStateFlow<List<CategoryTable>>(emptyList())
    val category : StateFlow<List<CategoryTable>> = _category
   init {
       viewModelScope.launch {
           insertDefaultCategories()
           repository.getAllcategory().collect { categories ->
               _category.value = categories
           }
       }
   }
    fun submitaddeddata(
        amount: Double,
        description:String,
        date: LocalDate,
        type: TransactionType,
        category: CategoryTable?,
        yearMonth: YearMonth

    ){
        viewModelScope.launch {
            _state.value = ApiResult.Loading()
            try {
               val addtranscation = TransactionTable(
                   amount = amount,
                   label = if (type == TransactionType.EXPENSE) category!!.label else "Income",
                   category = if (type == TransactionType.EXPENSE) category!!.label else "Income",
                   type = type.name,
                   date = date.toString(),
                   year = yearMonth.year,
                   month = yearMonth.monthValue,
                   description = description

               )
                if(type == TransactionType.EXPENSE) {
                    repository.addTransaction(addtranscation)
                }else{
                    repository.insertIncome(
                        year = yearMonth.year,
                        month =  yearMonth.monthValue,
                        amount = amount,
                        description = description,
                        date = date.toString()


                    )
                }
                _state.value = ApiResult.Success(addtranscation)

                Log.d("transcation", addtranscation.toString())
                Log.d("transcatio  n", "${type.name} ${category?.label}  ${category?.label}")

            }catch (e: Exception){
                _state.value = ApiResult.Error(e.message ?: "")



            }
        }
    }
    suspend fun insertDefaultCategories() {

        if (repository.getCategoryCount() == 0) {

            val categories = listOf(

                CategoryTable(
                    label = "Food & Dining",
                    emoji = "🍽️",
                    colorHex = 0xFFFF6B6B,
                    type = TransactionType.EXPENSE
                ),

                CategoryTable(
                    label = "Transport",
                    emoji = "🚇",
                    colorHex = 0xFF4ECDC4,
                    type = TransactionType.EXPENSE
                ),

                CategoryTable(
                    label = "Shopping",
                    emoji = "🛍️",
                    colorHex = 0xFFFFE66D,
                    type = TransactionType.EXPENSE
                ),

                CategoryTable(
                    label = "Health",
                    emoji = "💊",
                    colorHex = 0xFFA8E6CF,
                    type = TransactionType.EXPENSE
                ),

                CategoryTable(
                    label = "Entertainment",
                    emoji = "🎬",
                    colorHex = 0xFFC77DFF,
                    type = TransactionType.EXPENSE
                ),

                CategoryTable(
                    label = "Utilities",
                    emoji = "💡",
                    colorHex = 0xFFF8A978,
                    type = TransactionType.EXPENSE
                ),

                CategoryTable(
                    label = "Education",
                    emoji = "📚",
                    colorHex = 0xFF64B5F6,
                    type = TransactionType.EXPENSE
                ),

                CategoryTable(
                    label = "Travel",
                    emoji = "✈️",
                    colorHex = 0xFF4DB6AC,
                    type = TransactionType.EXPENSE
                ),

                CategoryTable(
                    label = "Bills",
                    emoji = "🧾",
                    colorHex = 0xFFFF8A65,
                    type = TransactionType.EXPENSE
                ),

                CategoryTable(
                    label = "Rent",
                    emoji = "🏠",
                    colorHex = 0xFF9575CD,
                    type = TransactionType.EXPENSE
                ),

                CategoryTable(
                    label = "Salary",
                    emoji = "💰",
                    colorHex = 0xFF66BB6A,
                    type = TransactionType.INCOME
                ),

                CategoryTable(
                    label = "Business",
                    emoji = "💼",
                    colorHex = 0xFF26A69A,
                    type = TransactionType.INCOME
                ),

                CategoryTable(
                    label = "Investment",
                    emoji = "📈",
                    colorHex = 0xFF42A5F5,
                    type = TransactionType.INCOME
                ),

                CategoryTable(
                    label = "Gift",
                    emoji = "🎁",
                    colorHex = 0xFFEC407A,
                    type = TransactionType.INCOME
                ),

                CategoryTable(
                    label = "Freelance",
                    emoji = "💻",
                    colorHex = 0xFF29B6F6,
                    type = TransactionType.INCOME
                ),

                CategoryTable(
                    label = "Bonus",
                    emoji = "🏆",
                    colorHex = 0xFFFFCA28,
                    type = TransactionType.INCOME
                ),

                CategoryTable(
                    label = "Refund",
                    emoji = "↩️",
                    colorHex = 0xFF7E57C2,
                    type = TransactionType.INCOME
                ),

                CategoryTable(
                    label = "Other",
                    emoji = "📦",
                    colorHex = 0xFF90A4AE,
                    type = TransactionType.EXPENSE
                )
            )

            categories.forEach {
                repository.insertcategory(it)
            }
        }

    }

}