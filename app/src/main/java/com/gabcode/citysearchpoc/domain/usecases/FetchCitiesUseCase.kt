package com.gabcode.citysearchpoc.domain.usecases

import com.gabcode.citysearchpoc.domain.repository.CityRepository
import javax.inject.Inject

class FetchCitiesUseCase @Inject constructor(
    private val cityRepository: CityRepository
) : UseCase<None, Result<Boolean>> {

    override suspend fun invoke(params: None): Result<Boolean> = cityRepository.fetchCities()
}
