package com.polotika.nearbyplacescleanarch.ui.feature.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.polotika.nearbyplacescleanarch.R
import com.polotika.nearbyplacescleanarch.core.common.BaseFragment
import com.polotika.nearbyplacescleanarch.core.common.DataState
import com.polotika.nearbyplacescleanarch.core.navigator.AppNavigator
import com.polotika.nearbyplacescleanarch.databinding.FragmentRestaurantMapsBinding
import com.polotika.nearbyplacescleanarch.domain.dto.LocationDto
import com.polotika.nearbyplacescleanarch.domain.entity.Restaurant
import com.polotika.nearbyplacescleanarch.domain.error.Failure
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RestaurantMapFragment : BaseFragment(), GoogleMap.OnMarkerClickListener {


    @Inject
    lateinit var appNavigator: AppNavigator

    var binding :FragmentRestaurantMapsBinding?=null

    private val viewModel :MapViewModel by viewModels()
    private var googleMap :GoogleMap? = null
    private val TAG = "RestaurantMapsFragment"
    private val LOCATION_REQUEST_CODE = 20001
    private val locationSettingsScreen =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            getCurrentLocation()
        }
    private val appSettingsScreen =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            getCurrentLocation()
        }
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {

                getCurrentLocation()
            } else
                showAlertDialog(
                    title = "Denied permission is required",
                    text = getString(R.string.permission_denied_message),
                    posBtnText = "RE-RTY",
                    posBtnListener = { d, _ ->
                        appSettingsScreen.launch(Intent(Settings.ACTION_APN_SETTINGS))
                    },
                    negBtnText = "",
                    negBtnListener = { d, _ ->
                        d.dismiss()
                    },
                )
        }

    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap


        googleMap.setOnMarkerClickListener(this)
        observers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantMapsBinding.inflate(inflater,container,false)
        return binding?.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                if (isLocationEnabled()) {
                    Log.d(TAG, "getCurrentLocation: enabled")

                    getLastKnownLocation {
                        Log.d(TAG, "lat: ${it.latitude.toString()}  long: ${it.longitude.toString()} ")
                        val location = LatLng(it.latitude, it.longitude)

                        val myLocationCircle = CircleOptions().center(location).radius(10000.0)
                            .strokeWidth(5f).fillColor(Color.TRANSPARENT).strokeColor(Color.BLUE)

                        googleMap?.isMyLocationEnabled = true

                        googleMap?.addCircle(myLocationCircle)

                        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location,12f),2500,null)

                        viewModel.getRestaurants(LocationDto(it.latitude,it.longitude))
                    }
                } else {
                    locationSettingsScreen.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }

            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                showAlertDialog(
                    title = "Location permission not enabled",
                    text = "Please allow location permission to use all app features.",
                    posBtnText = "Okay",
                    posBtnListener = { dialog, _ ->
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        dialog.dismiss()
                    })
            }
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED -> {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                Toast.makeText(requireContext(), "permission Denies", Toast.LENGTH_SHORT).show()
            }

            else -> {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_REQUEST_CODE && grantResults[0]
                .equals(Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            getLastKnownLocation {
                Log.d(TAG, "lat: ${it.latitude.toString()}  long: ${it.longitude.toString()} ")

            }
        } else Toast.makeText(requireContext(), "Request denied", Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {

        appNavigator.navigateTo(AppNavigator.Screen.RESTAURANT)
        return false
    }

    private fun observers(){
        viewModel.restaurantLiveData.observe(viewLifecycleOwner,{dataState->
            when(dataState){
                is DataState.Success ->{
                    handleLoader(false)
                    renderRestaurantMarkers(dataState.data)
                }
                is DataState.Error ->{
                    handleLoader(false)
                    if (dataState.error is Failure.NetworkConnection){
                        showError(getString(R.string.network_error))
                    }else{
                        showError(getString(R.string.general_error))
                    }

                }
                is DataState.Loading ->{
                    handleLoader(true)
                    Log.d(TAG, "observers: loading")
                }

            }
        })
    }

    private fun renderRestaurantMarkers(list:List<Restaurant>){

        list.forEach { restaurant ->
            val location = LatLng(restaurant.latitude, restaurant.longitude)
            googleMap?.addMarker(MarkerOptions().position(location).title(restaurant.name))
        }
    }

    private fun handleLoader(isLoading:Boolean){
        when(isLoading){
            true -> binding?.progressBar?.visibility = View.VISIBLE

            false -> binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun showError(message:String){
        Snackbar.make(binding!!.mainView,message,Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}