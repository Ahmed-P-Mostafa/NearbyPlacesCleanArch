package com.polotika.nearbyplacescleanarch.core.common

import com.polotika.nearbyplacescleanarch.domain.error.Failure

sealed class DataState<out T> {

    data class Success<out T>(val data:T):DataState<T>()
    data class Error(val error: Failure):DataState<Nothing>()
    object Loading:DataState<Nothing>()
}