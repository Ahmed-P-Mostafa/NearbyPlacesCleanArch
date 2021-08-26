package com.polotika.nearbyplacescleanarch.data.response

data class LabeledLatLng(
    val label: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0
)