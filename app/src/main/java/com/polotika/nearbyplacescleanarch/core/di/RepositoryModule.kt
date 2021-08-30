package com.polotika.nearbyplacescleanarch.core.di

import com.polotika.nearbyplacescleanarch.data.api.RestaurantDto
import com.polotika.nearbyplacescleanarch.data.repository.RestaurantRepositoryImpl
import com.polotika.nearbyplacescleanarch.domain.repository.RestaurantRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideRestaurantRepository(apis:RestaurantDto):RestaurantRepository{
        return RestaurantRepositoryImpl(apis)
    }
}