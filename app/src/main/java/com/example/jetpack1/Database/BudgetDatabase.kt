package com.example.jetpack1.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.jetpack1.Database.Dao.BudgetDao
import com.example.jetpack1.Database.Dao.IncomeDao
import com.example.jetpack1.Database.Dao.TranscationDao

import com.example.jetpack1.Database.Table.BudgetTable
import com.example.jetpack1.Database.Table.MonthlyIncomeTable
import com.example.jetpack1.Database.Table.TransactionTable

@TypeConverters(Converters::class)
@Database(entities = [TransactionTable::class, BudgetTable::class, MonthlyIncomeTable::class], version = 5, exportSchema = false)
public  abstract class BudgetDatabase: RoomDatabase() {

    abstract fun transactiobDao() : TranscationDao
    abstract fun budgetDao(): BudgetDao
    abstract fun incomeDao(): IncomeDao
}