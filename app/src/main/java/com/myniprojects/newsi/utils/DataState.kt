package com.myniprojects.newsi.utils

sealed class DataState<out R>
{
    data class Success<out T>(val data: T) : DataState<T>()

    data class Error<out T>(val data: T, val exception: Exception) : DataState<T>()

    object Loading : DataState<Nothing>()
}

val <T> T.exhaustive: T
    get() = this