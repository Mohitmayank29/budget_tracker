package com.budget.tracker.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

// --- Type Converters ---
class Converters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.toString()

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun fromCategory(category: Category): String = category.name

    @TypeConverter
    fun toCategory(value: String): Category = Category.valueOf(value)

    @TypeConverter
    fun fromType(type: TransactionType): String = type.name

    @TypeConverter
    fun toType(value: String): TransactionType = TransactionType.valueOf(value)
}

// --- DAOs ---
@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE year = :year AND month = :month ORDER BY date DESC")
    fun getTransactionsByMonth(year: Int, month: Int): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets WHERE year = :year AND month = :month")
    fun getBudgetsByMonth(year: Int, month: Int): Flow<List<Budget>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(budget: Budget)
}

@Dao
interface IncomeDao {
    @Query("SELECT * FROM monthly_income WHERE year = :year AND month = :month LIMIT 1")
    fun getIncome(year: Int, month: Int): Flow<MonthlyIncomeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(income: MonthlyIncomeEntity)
}

// --- Extra Entities ---
@Entity(tableName = "budgets", primaryKeys = ["year", "month", "category"])
data class Budget(
    val year: Int,
    val month: Int,
    val category: Category,
    val amount: Double
)

@Entity(tableName = "monthly_income", primaryKeys = ["year", "month"])
data class MonthlyIncomeEntity(
    val year: Int,
    val month: Int,
    val amount: Double
)

// --- Database ---
@Database(
    entities = [Transaction::class, Budget::class, MonthlyIncomeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun incomeDao(): IncomeDao
    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
