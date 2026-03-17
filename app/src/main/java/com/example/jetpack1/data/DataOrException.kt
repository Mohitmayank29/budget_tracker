package com.example.jetpack1.data

data class DataOrException<T, E:Exception>(
    var data:T?=null,
    var loading:Boolean = false,
    var e:E?=null
)