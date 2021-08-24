package com.polotika.nearbyplacescleanarch.domain.error

interface ErrorHandler {

    fun getError(throwable: Throwable) :Failure
}