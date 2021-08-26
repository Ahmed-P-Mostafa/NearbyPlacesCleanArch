package com.polotika.nearbyplacescleanarch.ui.feature.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.polotika.nearbyplacescleanarch.R
import com.polotika.nearbyplacescleanarch.core.common.BaseFragment
import com.polotika.nearbyplacescleanarch.core.navigator.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RestaurantMapFragment : BaseFragment(), GoogleMap.OnMarkerClickListener {

    @Inject
    lateinit var appNavigator: AppNavigator
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
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.setOnMarkerClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant_maps, container, false)
    }

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
                        Log.d(
                            TAG,
                            "lat: ${it.latitude.toString()}  long: ${it.longitude.toString()} ")
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


}