package com.budget.tracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.budget.tracker.data.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

data class UiState(
    val transactions: List<Transaction> = emptyList(),
    val budgets: List<Budget> = emptyList(),
    val income: Double = 0.0,
    val selectedMonth: YearMonth = YearMonth.now()
) {
    val expenses: Double get() = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    val balance: Double get() = income - expenses
    val expensesByCategory: Map<Category, Double> get() =
        transactions.filter { it.type == TransactionType.EXPENSE }
            .groupBy { it.category }
            .mapValues { (_, list) -> list.sumOf { it.amount } }
}

@OptIn(ExperimentalCoroutinesApi::class)
class BudgetViewModel(private val repo: BudgetRepository) : ViewModel() {

    private val _selectedMonth = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<UiState> = _selectedMonth.flatMapLatest { ym ->
        combine(
            repo.getTransactionsByMonth(ym.year, ym.monthValue),
            repo.getBudgetsByMonth(ym.year, ym.monthValue),
            repo.getIncome(ym.year, ym.monthValue)
        ) { transactions, budgets, income ->
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

    fun addTransaction(amount: Double, label: String, category: Category, type: TransactionType, date: LocalDate) {
        viewModelScope.launch {
            repo.addTransaction(
                Transaction(
                    amount = amount,
                    label = label.ifBlank { category.label },
                    category = category,
                    type = type,
                    date = date
                )
            )
        }
    }

    fun deleteTransaction(id: Long) = viewModelScope.launch { repo.deleteTransaction(id) }

    fun setBudget(category: Category, amount: Double) {
        val ym = _selectedMonth.value
        viewModelScope.launch {
            repo.upsertBudget(Budget(ym.year, ym.monthValue, category, amount))
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
