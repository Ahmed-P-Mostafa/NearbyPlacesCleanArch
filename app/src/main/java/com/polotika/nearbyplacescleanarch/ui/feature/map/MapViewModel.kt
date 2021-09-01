package com.polotika.nearbyplacescleanarch.ui.feature.map

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.Marker
import com.polotika.nearbyplacescleanarch.core.common.BaseViewModel
import com.polotika.nearbyplacescleanarch.core.common.DataState
import com.polotika.nearbyplacescleanarch.domain.dto.RequestDto
import com.polotika.nearbyplacescleanarch.domain.entity.Restaurant
import com.polotika.nearbyplacescleanarch.domain.interactor.GetRestaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val getRestaurant: GetRestaurant) : BaseViewModel() {

    val restaurantLiveData = MutableLiveData<DataState<List<Restaurant>>>()

    val markers = HashMap<Marker, Restaurant>()

    fun getRestaurants(locationDto: RequestDto) {
        if (restaurantLiveData.value != null) return

        restaurantLiveData.value = DataState.Loading

        getRestaurant.execute(locationDto).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe { restaurant ->
            restaurantLiveData.value = restaurant
        }.also { compositeDisposable.add(it) }
    }

    fun resetRestaurantState() {
        restaurantLiveData.value = null
    }

    fun getNewRestaurants(restaurants: List<Restaurant>): ArrayList<Restaurant> {
        val distinctMarkers = ArrayList<Restaurant>()
        val mainList = markers.values
        if (mainList.isNotEmpty()) {
            restaurants.forEach {
                if (!mainList.contains(it)) {
                    distinctMarkers.add(it)
                }
            }
        } else {
            distinctMarkers.addAll(restaurants)
        }

        return distinctMarkers
    }
}