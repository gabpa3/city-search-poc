package com.gabcode.citysearchpoc.data.repository

import android.util.Log
import com.gabcode.citysearchpoc.data.CityDto
import com.gabcode.citysearchpoc.data.CityTrie
import com.gabcode.citysearchpoc.data.database.CityDao
import com.gabcode.citysearchpoc.data.database.CityEntity
import com.gabcode.citysearchpoc.data.mapResult
import com.gabcode.citysearchpoc.data.service.CityService
import com.gabcode.citysearchpoc.data.service.makeRequest
import com.gabcode.citysearchpoc.data.toDomain
import com.gabcode.citysearchpoc.data.mapToDomain
import com.gabcode.citysearchpoc.data.toEntity
import com.gabcode.citysearchpoc.di.IODispatcher
import com.gabcode.citysearchpoc.domain.City
import com.gabcode.citysearchpoc.domain.repository.CityRepository
import javax.inject.Inject
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CityRepositoryImpl @Inject constructor(
    private val cityService: CityService,
    private val cityDao: CityDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : CityRepository {

    private val cityTrie = CityTrie()

    override suspend fun fetchCities(): Result<Boolean> = withContext(ioDispatcher) {
        makeRequest { cityService.getCities() }.also { result ->
            if (result.isSuccess) {
                val dataDto: List<CityDto>? = result.getOrNull()
                dataDto?.let {
                    launch {
                        val data = dataDto.toEntity()
                        val time = measureTimeMillis {
                            cityDao.insertAll(data)
//                            data.forEach { entity ->
//                                cityDao.insertFts(CityFtsEntity(entity.id, entity.name, entity.country))
//                            }
                        }
                        Log.i("Repository","DB insertion Insert All time $time ms")
                    }
                }
            }
        }.mapResult { citiesDto -> citiesDto.isNotEmpty() }
    }

    override fun searchCities(prefix: String): Flow<List<City>> = flow<List<City>> {
        val result: Flow<List<CityEntity>>
        val time = measureNanoTime {
            result = cityDao.getCitiesByPrefix(prefix)
        }
        Log.i("Repository", "DB Search prefix: $prefix time: $time ns")
        result.map { it.mapToDomain() }.collect { emit(it) }
    }.flowOn(ioDispatcher)

    override suspend fun saveFavorite(cityId: Int, isFavorite: Boolean) = withContext(ioDispatcher) {
        cityDao.updateFavoriteStatus(cityId, isFavorite)
    }

    suspend fun fetchCitiesOptional(): Result<Boolean> = withContext(ioDispatcher) {
        makeRequest { cityService.getCities() }.also { result ->
            if (result.isSuccess) {
                val dataDto: List<CityDto>? = result.getOrNull()
                dataDto?.let {
                    launch {
                        val cities = dataDto.toDomain()
                        val time = measureTimeMillis {
                            cities.onEach { city -> cityTrie.insert(city) }
                        }
                        Log.i("Repository","Trie Insert All time $time ms")
                    }
                }
            }
        }.mapResult { citiesDto -> citiesDto.isNotEmpty() }
    }

    suspend fun searchCitiesOptional(prefix: String): List<City> = withContext(ioDispatcher) {
        val result: List<City>
        val time = measureTimeMillis {
            result = cityTrie.search(prefix) // Trie
        }
        Log.i("Repository", "Trie Search prefix: $prefix time: $time ms")

        result
    }
}
