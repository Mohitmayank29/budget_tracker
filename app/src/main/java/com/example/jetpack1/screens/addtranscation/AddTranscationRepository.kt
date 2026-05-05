package com.example.jetpack1.screens.addtranscation

import com.example.jetpack1.Database.Dao.TranscationDao
import com.example.jetpack1.Database.Table.TransactionTable
import javax.inject.Inject

class AddTranscationRepository @Inject constructor(
    private val transactionDao : TranscationDao
) {

    suspend fun addTransaction(transaction: TransactionTable) =
        transactionDao.insert(transaction)

}