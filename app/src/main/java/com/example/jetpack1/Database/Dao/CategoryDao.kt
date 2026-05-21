package com.example.jetpack1.Database.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jetpack1.Database.Table.CategoryTable
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao{

    @Query("SELECT * FROM category_table")
    fun getAllCategory() : Flow<List<CategoryTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert( category : CategoryTable)

    @Query("DELETE FROM category_table WHERE id  = :id")
    suspend fun deletebyid(id:Int)
}