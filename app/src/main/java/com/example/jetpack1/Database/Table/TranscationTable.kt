package com.example.jetpack1.Database.Table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "transactions_table")

class TransactionTable (
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo (name = "amount") val amount: Double,
        @ColumnInfo (name = "label") val label: String,
        @ColumnInfo (name = "category") val category: String,
        @ColumnInfo (name = "type") val type: String,
        @ColumnInfo(name = "date") val date: Long,
        @ColumnInfo(name = "year") val year: Int,
        @ColumnInfo(name = "month") val month: Int
    )
