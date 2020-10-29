package com.myniprojects.newsi.utils

sealed class DataState<out R>
{
    data class Success<out T>(val data: T) : DataState<T>()

    data class Error(val exception: Exception) : DataState<Nothing>()

    object Loading : DataState<Nothing>()
}

val <T> T.exhaustive: T
    get() = this