package com.polotika.nearbyplacescleanarch.core.di

import android.content.Context
import com.polotika.nearbyplacescleanarch.BuildConfig
import com.polotika.nearbyplacescleanarch.core.common.AppConstants.BASE_URL
import com.polotika.nearbyplacescleanarch.core.common.AppConstants.TIME_OUT_VALUE
import com.polotika.nearbyplacescleanarch.data.api.RestaurantDto
import com.polotika.nearbyplacescleanarch.data.api.interceptors.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.math.log

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
        return loggingInterceptor
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(@ApplicationContext context: Context): AuthInterceptor {
        return AuthInterceptor(context)
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(TIME_OUT_VALUE, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT_VALUE, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT_VALUE, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient):Retrofit.Builder{
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
    }

    @Singleton
    @Provides
    fun provideFourSquareApis(retrofit: Retrofit.Builder):RestaurantDto{
        return retrofit.build().create(RestaurantDto::class.java)
    }
}