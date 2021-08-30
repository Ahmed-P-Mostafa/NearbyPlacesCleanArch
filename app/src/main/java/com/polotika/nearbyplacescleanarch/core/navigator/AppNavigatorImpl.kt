package com.polotika.nearbyplacescleanarch.core.navigator

import androidx.fragment.app.FragmentActivity
import com.polotika.nearbyplacescleanarch.R
import com.polotika.nearbyplacescleanarch.ui.feature.map.RestaurantMapFragment
import com.polotika.nearbyplacescleanarch.ui.feature.restaurant.RestaurantDetailsFragment
import javax.inject.Inject

class AppNavigatorImpl @Inject constructor(private val activity: FragmentActivity) : AppNavigator {

    //TODO add fragments here when we have new fragment added
    override fun navigateTo(screen: AppNavigator.Screen) {
        val fragment = when (screen) {
            AppNavigator.Screen.MAP -> RestaurantMapFragment()
            AppNavigator.Screen.RESTAURANT -> RestaurantDetailsFragment()
        }
        activity.supportFragmentManager.beginTransaction().replace(R.id.homeContainer, fragment)
            .addToBackStack(fragment.javaClass.canonicalName).commit()

    }
}