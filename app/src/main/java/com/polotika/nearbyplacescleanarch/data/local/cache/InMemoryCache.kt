package com.polotika.nearbyplacescleanarch.data.local.cache

import com.polotika.nearbyplacescleanarch.domain.entity.Restaurant

object InMemoryCache {
    private val cache = ArrayList<Restaurant>()

    fun get() = cache


    fun add(list: List<Restaurant>){
        cache.addAll(list)
    }
}