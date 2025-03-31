package com.gabcode.citysearchpoc.ui

sealed class UIState<out T> {
    data object Initial : UIState<Nothing>()
    data object Loading : UIState<Nothing>()
    data class Success<out T>(val data: T) : UIState<T>()
    data class Error(val message: String) : UIState<Nothing>()

    val isLoading
        get() = this is Loading

    val isSuccess
        get() = this is Success

    val isError
        get() = this is Error
}

