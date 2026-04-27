package com.budget.tracker.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.budget.tracker.data.*
import com.example.jetpack1.Database.Table.BudgetTable
import com.example.jetpack1.Database.Table.TransactionTable
import com.example.jetpack1.data.BudgetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import com.example.jetpack1.Database.Table.MonthlyIncomeTable
data class UiState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val transactions: List<TransactionTable> = emptyList(),
    val budgets: List<BudgetTable> = emptyList(),
    val income: Double = 0.0,
    val selectedMonth: YearMonth = YearMonth.now()
) {
    val expenses: Double get() = transactions.filter { TransactionType.entries.find{ t-> t.name == it.type } == TransactionType.EXPENSE }.sumOf { it.amount }
    val balance: Double get() = income - expenses
    val expensesByCategory: Map<Category, Double> get() =
        transactions.filter { TransactionType.valueOf(it.type) == TransactionType.EXPENSE }
            .groupBy { Category.valueOf(it.category) }
            .mapValues { (_, list) -> list.sumOf { it.amount } }
}

@OptIn(ExperimentalCoroutinesApi::class)
class BudgetViewModel(private val repo: BudgetRepository) : ViewModel() {

    private val _selectedMonth = MutableStateFlow(YearMonth.now())

    @RequiresApi(Build.VERSION_CODES.O)
    val uiState: StateFlow<UiState> = _selectedMonth.flatMapLatest { ym ->
        combine(
            repo.getTransactionsByMonth(ym.year, ym.monthValue),
            repo.getBudgetsByMonth(ym.year, ym.monthValue),
            repo.getIncome(ym.year, ym.monthValue)
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
            repo.addTransaction(
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

    fun deleteTransaction(id: Int) = viewModelScope.launch { repo.deleteTransaction(id) }

    fun setBudget(category: Category, amount: Double) {
        val ym = _selectedMonth.value
        viewModelScope.launch {
            repo.upsertBudget(BudgetTable(ym.year, ym.monthValue, category, amount))
        }
    }

    fun setIncome(amount: Double) {
        val ym = _selectedMonth.value
        viewModelScope.launch { repo.upsertIncome(ym.year, ym.monthValue, amount) }
    }
}

class BudgetViewModelFactory(
    private val repository: BudgetRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
