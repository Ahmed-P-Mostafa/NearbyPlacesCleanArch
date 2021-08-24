package com.polotika.nearbyplacescleanarch.domain.interactor

interface UseCase<T,R> {

    fun execute(param:T):R
}