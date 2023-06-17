package com.example.googledeveloperscommunityvisualisationtool.DataFetching.OldData.OldEvents.OldEventsDataClass

data class Venue(
    val address_1: String,
    val address_2: String,
    val city: String,
    val country: String,
    val id: Int,
    val lat: Double,
    val localized_country_name: String,
    val lon: Double,
    val name: String,
    val phone: String,
    val repinned: Boolean,
    val state: String,
    val zip: String
)