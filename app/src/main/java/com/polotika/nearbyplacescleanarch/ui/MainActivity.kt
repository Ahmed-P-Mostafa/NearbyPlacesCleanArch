package com.polotika.nearbyplacescleanarch.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import com.polotika.nearbyplacescleanarch.R
import com.polotika.nearbyplacescleanarch.core.navigator.AppNavigator
import com.polotika.nearbyplacescleanarch.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mInterstitialAd: InterstitialAd? = null
    private val TAG = "MainActivity"

    @Inject
    lateinit var appNavigator: AppNavigator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this) {  }

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,getString(R.string.adMob_ad_unit_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.message)
                Log.d(TAG, "onAdFailedToLoad: ${adError.code}")
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
        appNavigator.navigateTo(AppNavigator.Screen.MAP)
        binding.splashBg.animate().translationY(-4000F).setDuration(1000).setStartDelay(2000)
        binding.splashText.animate().translationY(4000F).setDuration(1000).setStartDelay(2000)
            .withEndAction {
                binding.splashBg.visibility = View.GONE
                binding.splashText.visibility = View.GONE

                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(this)
                } else {
                    Snackbar.make(binding.root,"The interstitial ad wasn't ready yet.",Snackbar.LENGTH_LONG).show()
                }
            }
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
                mInterstitialAd = null
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0)
            finish()
    }
}