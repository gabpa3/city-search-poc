package com.gabcode.citysearchpoc.data

import com.google.gson.annotations.SerializedName

data class CityDto(
    val name: String,
    val country: String,
    @SerializedName("_id") val id: Int,
    @SerializedName("coord") val coordinates: CoordinatesDto
)

data class CoordinatesDto(val lon: Double, val lat: Double)
