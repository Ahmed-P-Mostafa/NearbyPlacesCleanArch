package com.polotika.nearbyplacescleanarch.core.navigator

import com.polotika.nearbyplacescleanarch.domain.entity.Restaurant

interface AppNavigator {
    fun navigateTo(screen:Screen,restaurant: Restaurant?=null)

    enum class Screen{
        MAP,
        RESTAURANT
    }
}