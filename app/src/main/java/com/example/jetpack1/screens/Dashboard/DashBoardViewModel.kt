package com.example.jetpack1.screens.Dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.Database.Table.BudgetTable
import com.example.jetpack1.Database.Table.MonthlyIncomeTable
import com.example.jetpack1.Database.Table.TransactionTable
import com.example.jetpack1.enumclasses.Category
import com.example.jetpack1.enumclasses.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

data class UiState(
    val transactions: List<TransactionTable> = emptyList(),
    val budgets: List<BudgetTable> = emptyList(),
    val income: Double = 0.0,
    val selectedMonth: YearMonth = YearMonth.now()
) {
    val expenses: Double get() = transactions.filter { TransactionType.valueOf(it.type) == TransactionType.EXPENSE }.sumOf { it.amount }
    val balance: Double get() = income - expenses
    val expensesByCategory: Map<Category, Double> get() =
        transactions.filter { TransactionType.valueOf(it.type) == TransactionType.EXPENSE }
            .groupBy { Category.valueOf(it.category) }
            .mapValues { (_, list) -> list.sumOf { it.amount } }
}
@HiltViewModel
class DashBoardViewModel  @Inject constructor(
    private  val repository: DashBoardRepository
) : ViewModel() {
    private val _selectedMonth = MutableStateFlow(YearMonth.now())

    @RequiresApi(Build.VERSION_CODES.O)
    val uiState: StateFlow<UiState> = _selectedMonth.flatMapLatest { ym ->
        combine(
            repository.getTransactionsByMonth(ym.year, ym.monthValue),
            repository.getBudgetsByMonth(ym.year, ym.monthValue),
            repository.getIncome(ym.year, ym.monthValue)
        ) { transactions: List<TransactionTable>,
            budgets: List<BudgetTable>,
            income: MonthlyIncomeTable? ->
            UiState(
                transactions = transactions,
                budgets = budgets,
                income = income?.amount ?: 0.0,
                selectedMonth = ym
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    fun nextMonth() {
        _selectedMonth.value = _selectedMonth.value.plusMonths(1)
    }

    fun prevMonth() {
        _selectedMonth.value = _selectedMonth.value.minusMonths(1)
    }

    fun addTransaction(amount: Double, label: String, category: Category, type: TransactionType, date: LocalDate, yearMonth: YearMonth) {
        viewModelScope.launch {
            repository.addTransaction(
                TransactionTable(
                    amount = amount,
                    label = label.ifBlank { category.label },
                    category = category.name,
                    type = type.name,
                    date = date.toEpochDay(),
                    year = yearMonth.year,
                    month = yearMonth.monthValue

                )
            )
        }
    }

    fun deleteTransaction(id: Int) = viewModelScope.launch { repository.deleteTransaction(id) }

    fun setBudget(category: Category, amount: Double) {
        val ym = _selectedMonth.value
        viewModelScope.launch {
            repository.upsertBudget(BudgetTable(ym.year, ym.monthValue, category, amount))
        }
    }

    fun setIncome(amount: Double) {
        val ym = _selectedMonth.value
        viewModelScope.launch { repository.upsertIncome(ym.year, ym.monthValue, amount) }
    }

}