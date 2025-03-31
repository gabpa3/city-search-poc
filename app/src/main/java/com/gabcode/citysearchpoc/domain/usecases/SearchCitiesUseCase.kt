package com.gabcode.citysearchpoc.domain.usecases

import com.gabcode.citysearchpoc.domain.City
import com.gabcode.citysearchpoc.domain.repository.CityRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SearchCitiesUseCase @Inject constructor(
    private val cityRepository: CityRepository,
) : UseCase<SearchCitiesUseCase.Params, Flow<List<City>>> {

    override suspend fun invoke(params: Params): Flow<List<City>> =
        cityRepository.searchCities(params.prefix)

    data class Params(val prefix: String)
}
