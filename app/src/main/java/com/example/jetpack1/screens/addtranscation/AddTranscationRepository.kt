package com.example.jetpack1.screens.addtranscation

import com.example.jetpack1.Database.Dao.IncomeDao
import com.example.jetpack1.Database.Dao.TranscationDao
import com.example.jetpack1.Database.Table.MonthlyIncomeTable
import com.example.jetpack1.Database.Table.TransactionTable
import javax.inject.Inject

class AddTranscationRepository @Inject constructor(
    private val transactionDao : TranscationDao,
    private val incomeDao: IncomeDao
) {

    suspend fun addTransaction(transaction: TransactionTable) =
        transactionDao.insert(transaction)

    suspend fun upsertIncome(year: Int, month: Int, amount: Double) =
        incomeDao.upsert(MonthlyIncomeTable(year, month, amount))
    suspend fun insertIncome(year: Int,month: Int,amount: Double,description:String,date:String) =
        incomeDao.insert(MonthlyIncomeTable(year,month,amount,description,date))
}