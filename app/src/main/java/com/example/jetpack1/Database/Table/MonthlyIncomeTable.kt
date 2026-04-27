package com.example.jetpack1.Database.Table

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "income_table",primaryKeys = ["year", "month"])
data class MonthlyIncomeTable(
    @ColumnInfo (name = "year") val year: Int,
    @ColumnInfo (name = "month") val month: Int,
    @ColumnInfo (name = "amount") val amount: Double,

)