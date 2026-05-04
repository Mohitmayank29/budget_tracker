package com.example.jetpack1.Database.Table

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.jetpack1.enumclasses.Category

@Entity(tableName = "budget_table",primaryKeys = ["year", "month", "category"])
data class BudgetTable(
    @ColumnInfo (name = "year") val year: Int,
    @ColumnInfo (name = "month") val month: Int,
    @ColumnInfo (name = "category") val category: Category,
    @ColumnInfo (name = "amount") val amount: Double,

    )
