package com.budget.tracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

enum class TransactionType { EXPENSE, INCOME }

enum class Category(
    val label: String,
    val emoji: String,
    val colorHex: Long
) {
    FOOD("Food & Dining", "🍽️", 0xFFFF6B6B),
    TRANSPORT("Transport", "🚇", 0xFF4ECDC4),
    SHOPPING("Shopping", "🛍️", 0xFFFFE66D),
    HEALTH("Health", "💊", 0xFFA8E6CF),
    ENTERTAINMENT("Entertainment", "🎬", 0xFFC77DFF),
    UTILITIES("Utilities", "💡", 0xFFF8A978),
    OTHER("Other", "📦", 0xFF98C1D9)
}

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val label: String,
    val category: Category,
    val type: TransactionType,
    val date: LocalDate,
    val year: Int = date.year,
    val month: Int = date.monthValue
)

data class MonthlyIncome(
    val year: Int,
    val month: Int,
    val amount: Double
)
