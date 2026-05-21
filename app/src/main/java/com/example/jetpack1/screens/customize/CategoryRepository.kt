package com.example.jetpack1.screens.customize

import com.example.jetpack1.Database.Dao.CategoryDao
import com.example.jetpack1.Database.Table.CategoryTable
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val dao : CategoryDao
) {
    suspend fun insertcategory(category: CategoryTable){
        dao.insert(category)
    }

    fun getAllcategory() = dao.getAllCategory()


    suspend fun deletecategory(id:Int){
        dao.deletebyid(id)
    }


}