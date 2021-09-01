package com.polotika.nearbyplacescleanarch.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Restaurant (
    val id:String,
    val name:String,
    val city:String,
    val address:String,
    val longitude:Double,
    val latitude:Double
    ):Parcelable