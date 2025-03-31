package com.gabcode.citysearchpoc.data

import com.gabcode.citysearchpoc.data.database.CityEntity
import com.gabcode.citysearchpoc.domain.City
import com.gabcode.citysearchpoc.domain.Coordinates

internal fun<T,R> Result<T>.mapResult(transform: (T) -> R): Result<R> =
    fold(
        onSuccess = { runCatching { transform(it) } },
        onFailure = { Result.failure(it)}
    )

internal fun List<CityDto>.toDomain(): List<City> =
    map { cityDto ->  cityDto.toDomain() }

private fun CityDto.toDomain(): City =
    City(id = id, country = country, name = name, coordinates = coordinates.toDomain())

private fun CoordinatesDto.toDomain(): Coordinates =
    Coordinates(latitude = lat, longitude = lon)

internal fun List<CityDto>.toEntity(): List<CityEntity> =
    map { cityDto ->  cityDto.toEntity() }

private fun CityDto.toEntity(): CityEntity =
    CityEntity(
        id = id,
        name = name,
        country = country,
        latitude = coordinates.lat,
        longitude = coordinates.lat
    )

internal fun List<CityEntity>.mapToDomain(): List<City> =
    map { cityEntity ->  cityEntity.mapToDomain() }

private fun CityEntity.mapToDomain(): City =
    City(
        id = id,
        country = country,
        name = name,
        coordinates = Coordinates(latitude = latitude, longitude = longitude),
        isFavorite = isFavorite
    )
