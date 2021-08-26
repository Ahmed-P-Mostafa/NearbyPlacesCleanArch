package com.polotika.nearbyplacescleanarch.data.response

data class Location(
    val address: String = "",
    val cc: String = "",
    val city: String = "",
    val country: String = "",
    val distance: Int = 0,
    val formattedAddress: List<String> = listOf(),
    val labeledLatLngs: List<LabeledLatLng> = listOf(),
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val state: String = ""
)