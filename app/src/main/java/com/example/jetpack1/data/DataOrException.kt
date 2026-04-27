package com.example.jetpack1.data

sealed class ApiResult <T> {
    class Loading<T> : ApiResult<T>()
    data class Success<T>(val data: T?) : ApiResult<T>()
    data class Error<T>(val message: String) : ApiResult<T>()
}