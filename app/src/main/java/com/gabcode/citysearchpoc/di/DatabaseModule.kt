package com.gabcode.citysearchpoc.di

import android.content.Context
import androidx.room.Room
import com.gabcode.citysearchpoc.data.database.AppDatabase
import com.gabcode.citysearchpoc.data.database.CityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "data.db"
    ).build()

    @Provides
    @Singleton
    fun provideCityDao(database: AppDatabase): CityDao = database.cityDao()
}
