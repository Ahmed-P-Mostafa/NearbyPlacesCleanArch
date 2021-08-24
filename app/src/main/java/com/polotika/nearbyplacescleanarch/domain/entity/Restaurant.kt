package com.polotika.nearbyplacescleanarch.domain.entity

import javax.sql.StatementEvent

data class Restaurant (
    val id:String,
    val name:String,
    val city:String,
    val address:String,
    val longitude:Double,
    val latitude:Double
    )