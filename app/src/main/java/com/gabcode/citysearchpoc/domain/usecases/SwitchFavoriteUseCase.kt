package com.gabcode.citysearchpoc.domain.usecases

import com.gabcode.citysearchpoc.domain.repository.CityRepository
import javax.inject.Inject

class SwitchFavoriteUseCase @Inject constructor(
    private val cityRepository: CityRepository
) : UseCase<SwitchFavoriteUseCase.Params, Unit> {

    override suspend fun invoke(params: Params) {
        cityRepository.saveFavorite(params.cityId, params.isFavorite)
    }

    data class Params(val cityId: Int, val isFavorite: Boolean)
}
