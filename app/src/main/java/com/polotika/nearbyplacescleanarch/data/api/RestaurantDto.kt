package com.polotika.nearbyplacescleanarch.data.api

import com.polotika.nearbyplacescleanarch.data.response.VenuesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantDto {

    @GET("v2/venues/search")
    fun getRestaurants(@Query("ll",encoded = true)ll:String):Single<VenuesResponse>
}