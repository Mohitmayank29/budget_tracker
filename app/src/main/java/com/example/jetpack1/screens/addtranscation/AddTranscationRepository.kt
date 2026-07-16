package com.example.jetpack1.screens.addtranscation

import com.example.jetpack1.Database.Dao.CategoryDao
import com.example.jetpack1.Database.Dao.IncomeDao
import com.example.jetpack1.Database.Dao.TranscationDao
import com.example.jetpack1.Database.Table.CategoryTable
import com.example.jetpack1.Database.Table.MonthlyIncomeTable
import com.example.jetpack1.Database.Table.TransactionTable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddTranscationRepository @Inject constructor(
    private val transactionDao : TranscationDao,
    private val incomeDao: IncomeDao,
    private val categoryDao: CategoryDao
) {

    suspend fun addTransaction(transaction: TransactionTable) =
        transactionDao.insert(transaction)

    suspend fun upsertIncome(year: Int, month: Int, amount: Double) =
        incomeDao.upsert(MonthlyIncomeTable(year, month, amount))
    suspend fun insertIncome(year: Int,month: Int,amount: Double,description:String,date:String) =
        incomeDao.insert(MonthlyIncomeTable(year,month,amount,description,date))

    fun getAllcategory() : Flow<List<CategoryTable>> =
         categoryDao.getAllCategory()

    suspend fun insertcategory(category: CategoryTable) =
        categoryDao.insert(category)
    suspend fun getCategoryCount(): Int =
        categoryDao.getCategoryCount()
}