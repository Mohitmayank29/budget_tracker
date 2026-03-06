package com.budget.tracker.data

import kotlinx.coroutines.flow.Flow

class BudgetRepository(
    private val transactionDao: TransactionDao,
    private val budgetDao: BudgetDao,
    private val incomeDao: IncomeDao
) {
    fun getTransactionsByMonth(year: Int, month: Int): Flow<List<Transaction>> =
        transactionDao.getTransactionsByMonth(year, month)

    fun getBudgetsByMonth(year: Int, month: Int): Flow<List<Budget>> =
        budgetDao.getBudgetsByMonth(year, month)

    fun getIncome(year: Int, month: Int): Flow<MonthlyIncomeEntity?> =
        incomeDao.getIncome(year, month)

    suspend fun addTransaction(transaction: Transaction) =
        transactionDao.insert(transaction)

    suspend fun deleteTransaction(id: Long) =
        transactionDao.deleteById(id)

    suspend fun upsertBudget(budget: Budget) =
        budgetDao.upsert(budget)

    suspend fun upsertIncome(year: Int, month: Int, amount: Double) =
        incomeDao.upsert(MonthlyIncomeEntity(year, month, amount))
}
