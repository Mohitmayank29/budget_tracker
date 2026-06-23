package com.example.jetpack1.Database

import android.content.Context
import androidx.room.Room
import com.example.jetpack1.Database.Dao.BudgetDao
import com.example.jetpack1.Database.Dao.CategoryDao
import com.example.jetpack1.Database.Dao.IncomeDao
import com.example.jetpack1.Database.Dao.TranscationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BudgetDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            BudgetDatabase::class.java,
            "budget_database"
        ).build()
    }

    @Provides
    fun provideBudgetDao(db: BudgetDatabase): BudgetDao {
        return db.budgetDao()
    }
    @Provides
    fun provideTranscationDao (db: BudgetDatabase): TranscationDao {
        return db.transactiobDao()
    }
    @Provides
    fun provideIncomeDao (db: BudgetDatabase): IncomeDao {
        return db.incomeDao()
    }
    @Provides
    fun provideCategoryDao (db: BudgetDatabase): CategoryDao {
        return db.categoryDao()
    }
}