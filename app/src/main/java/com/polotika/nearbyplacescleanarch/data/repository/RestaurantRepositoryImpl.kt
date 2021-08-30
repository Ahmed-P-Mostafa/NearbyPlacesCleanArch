package com.polotika.nearbyplacescleanarch.data.repository

import com.polotika.nearbyplacescleanarch.core.common.DataState
import com.polotika.nearbyplacescleanarch.data.api.RestaurantDto
import com.polotika.nearbyplacescleanarch.domain.dto.LocationDto
import com.polotika.nearbyplacescleanarch.domain.entity.Restaurant
import com.polotika.nearbyplacescleanarch.domain.repository.RestaurantRepository
import io.reactivex.Single

class RestaurantRepositoryImpl(private val apis: RestaurantDto) : RestaurantRepository {
    override fun getRestaurant(locationDto: LocationDto): Single<DataState<List<Restaurant>>> {
        return apis.getRestaurants("${locationDto.lat},${locationDto.lang}").map {
            val restList = arrayListOf<Restaurant>()

            it.response.venues.forEach { rest ->
                val newRest = Restaurant(
                    id = rest.id,
                    name = rest.name,
                    city = rest.location.city,
                    address = rest.location.address,
                    longitude = rest.location.lng,
                    latitude = rest.location.lat
                )
                restList.add(newRest)
            }
            DataState.Success(restList)
        }
    }
}