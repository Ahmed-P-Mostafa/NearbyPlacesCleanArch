package com.polotika.nearbyplacescleanarch.data.response

data class Category(
    val icon: Icon = Icon(),
    val id: String = "",
    val name: String = "",
    val pluralName: String = "",
    val primary: Boolean = false,
    val shortName: String = ""
)