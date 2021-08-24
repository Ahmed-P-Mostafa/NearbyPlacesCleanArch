package com.polotika.nearbyplacescleanarch.domain.error

sealed class Failure {
    object NetworkConnection :Failure()
    sealed class ServerError :Failure(){
        object NotFound :ServerError()
        object ServerUnavailable :ServerError()
        object AccessDenied :ServerError()
    }
    object UnknownError :Failure()
}