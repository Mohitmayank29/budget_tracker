package com.example.jetpack1.Database.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jetpack1.Database.Table.MonthlyIncomeTable
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {
    @Query("SELECT * FROM income_table WHERE year = :year AND month = :month LIMIT 1")
    fun getIncome(year: Int, month: Int): Flow<MonthlyIncomeTable?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(income: MonthlyIncomeTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(income: MonthlyIncomeTable)
}