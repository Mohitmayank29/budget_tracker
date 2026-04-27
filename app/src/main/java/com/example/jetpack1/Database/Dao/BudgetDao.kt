package com.example.jetpack1.Database.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jetpack1.Database.Table.BudgetTable
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budget_table WHERE year = :year AND month = :month")
    fun getBudgetsByMonth(year: Int, month: Int): Flow<List<BudgetTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(budget: BudgetTable)
}