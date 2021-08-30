package com.polotika.nearbyplacescleanarch.ui.feature.map

import androidx.lifecycle.MutableLiveData
import com.polotika.nearbyplacescleanarch.core.common.BaseViewModel
import com.polotika.nearbyplacescleanarch.core.common.DataState
import com.polotika.nearbyplacescleanarch.domain.dto.LocationDto
import com.polotika.nearbyplacescleanarch.domain.entity.Restaurant
import com.polotika.nearbyplacescleanarch.domain.interactor.GetRestaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val getRestaurant: GetRestaurant) :BaseViewModel() {

    val restaurantLiveData = MutableLiveData<DataState<List<Restaurant>>>()

    fun getRestaurants(locationDto: LocationDto){
        if (restaurantLiveData.value!=null) return

        getRestaurant.execute(locationDto).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe{ restaurant->
            restaurantLiveData.value = restaurant
        }.also { compositeDisposable.add(it) }
    }
}