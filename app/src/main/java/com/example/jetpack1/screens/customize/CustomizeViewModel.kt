package com.example.jetpack1.screens.customize

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.Database.Table.CategoryTable
import com.example.jetpack1.data.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomizeViewModel @Inject constructor(
    private val repository: CategoryRepository
): ViewModel() {

  private val _state = MutableStateFlow<ApiResult<List<CategoryTable>>?>(null)
    val state : StateFlow<ApiResult<List<CategoryTable>>?> = _state

    init {
        getAllCategories()
    }
    private fun getAllCategories() {

        viewModelScope.launch {

            repository.getAllcategory().collect { list ->
                Log.d("CATEGORY", "Size = ${list.size}")

                _state.value = ApiResult.Success(list)
            }
        }
    }
    fun insertcategory(category : CategoryTable){
        viewModelScope.launch {

            _state.value = ApiResult.Loading()
            try {
                Log.d("CATEGORY", "Before Insert")
                repository.insertcategory(category)
                Log.d("CATEGORY", "After Insert")
            }
            catch (e: Exception) {
                _state.value = ApiResult.Error(e.message ?: "Somethinbg went Wrong!")
            }
        }

    }
    fun deletecategory(id:Int){
        viewModelScope.launch {
            _state.value = ApiResult.Loading()
            try {
                repository.deletecategory(id)

            }catch (e: Exception){
                _state.value = ApiResult.Error(e.message ?: "SomeThing Went Wrong!")
            }
        }
    }
}