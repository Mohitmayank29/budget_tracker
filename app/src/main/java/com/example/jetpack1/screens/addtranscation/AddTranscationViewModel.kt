package com.example.jetpack1.screens.addtranscation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack1.Database.Dao.TranscationDao
import com.example.jetpack1.Database.Table.TransactionTable
import com.example.jetpack1.data.ApiResult
import com.example.jetpack1.enumclasses.Category
import com.example.jetpack1.enumclasses.TransactionType
import com.google.android.gms.common.api.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class AddTranscationViewModel @Inject constructor(
    private val repository: AddTranscationRepository,
    private val transcationDao: TranscationDao
): ViewModel() {

   val _state = MutableStateFlow<ApiResult<TransactionTable>?>(null)
    val state : StateFlow<ApiResult<TransactionTable>?> =_state
    fun submitaddeddata(
        amount: Double,
        description:String,
        date: LocalDate,
        type: TransactionType,
        category: Category?,
        yearMonth: YearMonth

    ){
        viewModelScope.launch {
            _state.value = ApiResult.Loading()
            try {
               val addtranscation = TransactionTable(
                   amount = amount,
                   label = if (type == TransactionType.EXPENSE) category!!.label else "Income",
                   category = if (type == TransactionType.EXPENSE) category!!.name else "Income",
                   type = type.name,
                   date = date.toString(),
                   year = yearMonth.year,
                   month = yearMonth.monthValue,
                   description = description

               )
                if(type == TransactionType.EXPENSE) {
                    repository.addTransaction(addtranscation)
                }else{
                    repository.insertIncome(
                        year = yearMonth.year,
                        month =  yearMonth.monthValue,
                        amount = amount,
                        description = description,
                        date = date.toString()


                    )
                }
                _state.value = ApiResult.Success(addtranscation)

                Log.d("transcation", addtranscation.toString())
                Log.d("transcatio  n", "${type.name} ${category?.label}  ${category?.name}")

            }catch (e: Exception){
                _state.value = ApiResult.Error(e.message ?: "")



            }
        }
    }

}