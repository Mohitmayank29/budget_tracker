package com.example.jetpack1.Database.Table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jetpack1.enumclasses.TransactionType


@Entity(tableName = "category_table")
data class CategoryTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = "id") val id: Int = 0,
    @ColumnInfo (name = "label") val label: String,
    @ColumnInfo (name = "emoji") val emoji: String,
    @ColumnInfo (name = "colorHex") val colorHex: Long,
    @ColumnInfo (name = "type") val type : TransactionType


)
