package com.polotika.nearbyplacescleanarch.domain.repository

import com.polotika.nearbyplacescleanarch.core.common.DataState
import com.polotika.nearbyplacescleanarch.domain.dto.LocationDto
import com.polotika.nearbyplacescleanarch.domain.dto.RequestDto
import com.polotika.nearbyplacescleanarch.domain.entity.Restaurant
import io.reactivex.Single

interface RestaurantRepository {
    fun getRestaurant (locationDto: RequestDto) : Single<DataState<List<Restaurant>>>
}