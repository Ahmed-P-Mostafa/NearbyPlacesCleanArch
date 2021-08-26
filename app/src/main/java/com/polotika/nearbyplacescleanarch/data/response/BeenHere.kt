package com.polotika.nearbyplacescleanarch.data.response

data class BeenHere(
    val count: Int = 0,
    val lastCheckinExpiredAt: Int = 0,
    val marked: Boolean = false,
    val unconfirmedCount: Int = 0
)