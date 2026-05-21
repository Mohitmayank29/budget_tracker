package com.example.jetpack1.screens.AddBudget

import com.example.jetpack1.Database.Dao.BudgetDao
import com.example.jetpack1.Database.Dao.IncomeDao
import com.example.jetpack1.Database.Dao.TranscationDao
import com.example.jetpack1.Database.Table.BudgetTable
import com.example.jetpack1.Database.Table.MonthlyIncomeTable
import com.example.jetpack1.Database.Table.TransactionTable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddBudgetRepository @Inject constructor(
    private val transactionDao: TranscationDao,
    private val budgetDao: BudgetDao,
    private val incomeDao: IncomeDao
) {
    fun getTransactionsByMonth(year: Int, month: Int): Flow<List<TransactionTable>> =
        transactionDao.getTransactionsByMonth(year, month)

    fun getBudgetsByMonth(year: Int, month: Int): Flow<List<BudgetTable>> =
        budgetDao.getBudgetsByMonth(year, month)

    fun getIncome(year: Int, month: Int): Flow<MonthlyIncomeTable?> =
        incomeDao.getIncome(year, month)

    suspend fun insertTransaction(transaction: TransactionTable) =
        transactionDao.insert(transaction)

    suspend fun deleteTransaction(id: Int) =
        transactionDao.deleteById(id)

    suspend fun upsertBudget(budget: BudgetTable) =
        budgetDao.upsert(budget)

    suspend fun upsertIncome(year: Int, month: Int, amount: Double) =
        incomeDao.upsert(MonthlyIncomeTable(year, month, amount))
}