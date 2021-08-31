package com.polotika.nearbyplacescleanarch.domain.interactor

import com.polotika.nearbyplacescleanarch.core.common.DataState
import com.polotika.nearbyplacescleanarch.domain.dto.LocationDto
import com.polotika.nearbyplacescleanarch.domain.dto.RequestDto
import com.polotika.nearbyplacescleanarch.domain.entity.Restaurant
import com.polotika.nearbyplacescleanarch.domain.error.ErrorHandler
import com.polotika.nearbyplacescleanarch.domain.error.Failure
import com.polotika.nearbyplacescleanarch.domain.repository.RestaurantRepository
import io.reactivex.Single
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.UnknownHostException
import javax.inject.Inject

class GetRestaurant @Inject constructor(private val repository: RestaurantRepository) :
    UseCase<RequestDto, Single<DataState<List<Restaurant>>>>, ErrorHandler {
    override fun execute(param: RequestDto): Single<DataState<List<Restaurant>>> {
        return repository.getRestaurant(param).onErrorReturn {
            DataState.Error(getError(it))
        }
    }

    override fun getError(throwable: Throwable): Failure {

        return when(throwable){
            is UnknownHostException -> Failure.NetworkConnection
            is HttpException-> {
                when(throwable.code()){
                    HttpURLConnection.HTTP_NOT_FOUND -> Failure.ServerError.NotFound
                    HttpURLConnection.HTTP_FORBIDDEN ->Failure.ServerError.AccessDenied
                    HttpURLConnection.HTTP_UNAVAILABLE ->Failure.ServerError.ServerUnavailable
                    else ->Failure.UnknownError
                }
            }
            else -> Failure.UnknownError
        }
    }
}