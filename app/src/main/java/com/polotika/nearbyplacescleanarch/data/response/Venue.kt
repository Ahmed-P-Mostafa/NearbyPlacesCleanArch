package com.polotika.nearbyplacescleanarch.data.response

data class Venue(
    val beenHere: BeenHere = BeenHere(),
    val categories: List<Category> = listOf(),
    val contact: Contact = Contact(),
    val hasPerk: Boolean = false,
    val hereNow: HereNow = HereNow(),
    val id: String = "",
    val location: Location = Location(),
    val name: String = "",
    val referralId: String = "",
    val stats: Stats = Stats(),
    val venueChains: List<Any> = listOf(),
    val verified: Boolean = false
)