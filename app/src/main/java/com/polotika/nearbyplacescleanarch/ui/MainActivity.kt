package com.polotika.nearbyplacescleanarch.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.polotika.nearbyplacescleanarch.R
import com.polotika.nearbyplacescleanarch.core.navigator.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var appNavigator: AppNavigator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appNavigator.navigateTo(AppNavigator.Screen.MAP)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(supportFragmentManager.backStackEntryCount==0)
            finish()
    }
}