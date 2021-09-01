package com.polotika.nearbyplacescleanarch.data.repository

import com.google.android.gms.maps.model.LatLng
import com.polotika.nearbyplacescleanarch.core.common.DataState
import com.polotika.nearbyplacescleanarch.data.api.RestaurantDto
import com.polotika.nearbyplacescleanarch.data.local.cache.InMemoryCache
import com.polotika.nearbyplacescleanarch.domain.dto.LocationDto
import com.polotika.nearbyplacescleanarch.domain.dto.RequestDto
import com.polotika.nearbyplacescleanarch.domain.entity.Restaurant
import com.polotika.nearbyplacescleanarch.domain.repository.RestaurantRepository
import io.reactivex.Single

class RestaurantRepositoryImpl(private val apis: RestaurantDto) : RestaurantRepository {
    override fun getRestaurant(locationDto: RequestDto): Single<DataState<List<Restaurant>>> {

        val cache = InMemoryCache.get()
        val filteredList = ArrayList<Restaurant>()
        cache.forEach {
            val latlng = LatLng(it.latitude,it.longitude)
            if (locationDto.latLngBounds.contains(latlng)){
                filteredList.add(it)
            }
        }

        if(filteredList.isNotEmpty()&& filteredList!= null){
            return Single.just(DataState.Success(filteredList))
        }
        return apis.getRestaurants("${locationDto.latLng.latitude},${locationDto.latLng.longitude}").map {
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

            // add data in memory cache
            InMemoryCache.add(restList)
            DataState.Success(restList)
        }
    }
}