package com.polotika.nearbyplacescleanarch.data.api.interceptors

import com.polotika.nearbyplacescleanarch.core.common.AppConstants.CLIENT_ID_KEY
import com.polotika.nearbyplacescleanarch.core.common.AppConstants.CLIENT_SECRET_KEY
import com.polotika.nearbyplacescleanarch.core.common.AppConstants.CURRENT_VERSION
import com.polotika.nearbyplacescleanarch.core.common.AppConstants.RESTAURANT_QUERY
import com.polotika.nearbyplacescleanarch.core.common.AppConstants.QUERY_KEY
import com.polotika.nearbyplacescleanarch.core.common.AppConstants.VERSION_KEY
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val originalUrl = request.url()
        val modifiedUrl = originalUrl.newBuilder()
            .addQueryParameter(CLIENT_ID_KEY,"")
            .addQueryParameter(CLIENT_SECRET_KEY,"")
            .addQueryParameter(VERSION_KEY, CURRENT_VERSION)
            .addQueryParameter(QUERY_KEY, RESTAURANT_QUERY).build()
        val modifiedRequest = request.newBuilder().url(modifiedUrl).build()

        return chain.proceed(modifiedRequest)
    }
}