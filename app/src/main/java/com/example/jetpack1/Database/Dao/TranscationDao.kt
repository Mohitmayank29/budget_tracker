package com.example.jetpack1.Database.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jetpack1.Database.Table.TransactionTable
import kotlinx.coroutines.flow.Flow

@Dao
interface TranscationDao {
    @Query("SELECT * FROM transactions_table WHERE year = :year AND month = :month ORDER BY date DESC")
        fun getTransactionsByMonth(year: Int, month: Int): Flow<List<TransactionTable>>

        @Query("SELECT * FROM transactions_table ORDER BY date DESC")
        fun getAllTransactions(): Flow<List<TransactionTable>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(transaction: TransactionTable)

        @Delete
        suspend fun delete(transaction: TransactionTable)

        @Query("DELETE FROM transactions_table WHERE id = :id")
        suspend fun deleteById(id: Int)

}