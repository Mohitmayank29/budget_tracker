package com.example.jetpack1.enumclasses

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
data class MonthlyIncome(
    val year: Int,
    val month: Int,
    val amount: Double
)