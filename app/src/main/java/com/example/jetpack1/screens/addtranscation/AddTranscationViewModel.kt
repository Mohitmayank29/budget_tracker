package com.example.jetpack1.screens.addtranscation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTranscationViewModel @Inject constructor(
    private val repository: AddTranscationRepository
): ViewModel() {


    fun submitaddeddata(){
        viewModelScope.launch {
            try {

            }catch (e: Exception){


            }
        }
    }

}