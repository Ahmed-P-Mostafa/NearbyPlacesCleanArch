package com.polotika.nearbyplacescleanarch.core.di

import com.polotika.nearbyplacescleanarch.core.navigator.AppNavigator
import com.polotika.nearbyplacescleanarch.core.navigator.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class NavigatorModule {

    @Binds
    abstract fun bindsAppNavigator(appNavigatorImpl: AppNavigatorImpl):AppNavigator
}