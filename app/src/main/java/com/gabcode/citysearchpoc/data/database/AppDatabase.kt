package com.gabcode.citysearchpoc.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

//@Database(entities = [CityEntity::class, CityFtsEntity::class], version = 1, exportSchema = false)
@Database(entities = [CityEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}
