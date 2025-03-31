package com.gabcode.citysearchpoc.domain

data class City(
    val id: Int,
    val name: String,
    val country: String,
    val coordinates: Coordinates,
    val isFavorite: Boolean = false
)

data class Coordinates(val longitude: Double, val latitude: Double)
