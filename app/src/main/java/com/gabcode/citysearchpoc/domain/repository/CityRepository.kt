package com.gabcode.citysearchpoc.domain.repository

import com.gabcode.citysearchpoc.domain.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    suspend fun fetchCities(): Result<Boolean>
    fun searchCities(prefix: String): Flow<List<City>>
    suspend fun saveFavorite(cityId: Int, isFavorite: Boolean)
}
