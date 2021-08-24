package com.polotika.nearbyplacescleanarch.core.navigator

interface AppNavigator {
    fun navigateTo(screen:Screen)

    enum class Screen{
        MAP,
        RESTAURANT
    }
}