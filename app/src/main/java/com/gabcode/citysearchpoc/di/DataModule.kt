package com.gabcode.citysearchpoc.di

import com.gabcode.citysearchpoc.data.repository.CityRepositoryImpl
import com.gabcode.citysearchpoc.domain.repository.CityRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindCityRepository(cityRepositoryImpl: CityRepositoryImpl): CityRepository
}
