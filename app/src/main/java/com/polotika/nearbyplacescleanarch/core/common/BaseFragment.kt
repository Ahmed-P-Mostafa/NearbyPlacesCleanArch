package com.polotika.nearbyplacescleanarch.core.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder

open class BaseFragment : Fragment() {
    private val fusedLocationClint: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(onLocationAvailable: (Location) -> Unit) {
        fusedLocationClint.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                onLocationAvailable(location)
            } else {
                createLocationRequest(onLocationAvailable)
            }

        }
    }

    private fun createLocationRequest(onLocationAvailable: (Location) -> Unit) {
        locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                for (location in result.locations) {
                    onLocationAvailable(location)
                }
            }
        }
        startLocationUpdates()
    }

    private fun stopLocationUpdates() {
        if (fusedLocationClint != null && locationCallback != null) {
            fusedLocationClint.removeLocationUpdates(locationCallback)
        }
    }

    fun showAlertDialog(
        title: String,
        text: String,
        posBtnText: String,
        negBtnText: String?,
        posBtnListener: DialogInterface.OnClickListener,
        negBtnListener: DialogInterface.OnClickListener?
    ) {
        MaterialAlertDialogBuilder(requireActivity()).setTitle(title).setMessage(text)
            .setPositiveButton(posBtnText, posBtnListener).setNegativeButton(
                negBtnText ?: "Cancel",
                negBtnListener ?: DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
    }

    fun showAlertDialog(
        title: String,
        text: String,
        posBtnText: String,

        posBtnListener: DialogInterface.OnClickListener,

        ) {
        MaterialAlertDialogBuilder(requireActivity()).setTitle(title).setMessage(text)
            .setPositiveButton(posBtnText, posBtnListener).setNegativeButton(
                "Cancel"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if(locationCallback!=null&&locationRequest!=null){

            fusedLocationClint.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        //startLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()

    }

    fun isLocationEnabled(): Boolean {
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}