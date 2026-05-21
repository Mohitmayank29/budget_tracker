package com.example.jetpack1.screens.AddBudget

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.Database.Table.BudgetTable
import com.example.jetpack1.data.ApiResult
import com.example.jetpack1.enumclasses.Category
import com.example.jetpack1.screens.Dashboard.UiState
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject


@HiltViewModel
class AddbudgetViewModel @Inject constructor(
    private  val repository: AddBudgetRepository

): ViewModel() {
    private val _selectedMonth = MutableStateFlow(YearMonth.now())
    private val _state = MutableStateFlow<ApiResult<AuthResult>?>(null)
    val  state : StateFlow<ApiResult<AuthResult>?> = _state
    @RequiresApi(Build.VERSION_CODES.O)

    val uiState: StateFlow<UiState> = _selectedMonth.flatMapLatest { ym ->
        combine(
            repository.getTransactionsByMonth(ym.year, ym.monthValue),
            repository.getBudgetsByMonth(ym.year, ym.monthValue),
            repository.getIncome(ym.year, ym.monthValue)
        ) { transactions, budgets, income ->
            UiState(
                transactions = transactions,
                budgets = budgets,
                income = income?.amount ?: 0.0,
                selectedMonth = ym
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

     fun setbudget(category: Category, amount: Double){
        val ym = _selectedMonth.value
        viewModelScope.launch {
            repository.upsertBudget(BudgetTable(ym.year, ym.monthValue, category, amount))
        }
    }




}