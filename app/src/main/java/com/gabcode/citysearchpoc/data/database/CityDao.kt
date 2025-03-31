package com.gabcode.citysearchpoc.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<CityEntity>)

    @Query("SELECT * FROM city WHERE name LIKE :prefix || '%' ORDER BY name ASC, country ASC")
    fun getCitiesByPrefix(prefix: String): Flow<List<CityEntity>>

    @Query("UPDATE city SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    @Query("SELECT EXISTS(SELECT * FROM city LIMIT 1)")
    suspend fun isEmpty(): Boolean

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertFts(city: CityFtsEntity)


//    @Query("""
//        SELECT * FROM city
//        JOIN city_fts ON city.id = city_fts.rowid
//        WHERE SUBSTR(city_fts.name, 1, LENGTH(:prefix)) = :prefix
//        ORDER BY city.name ASC, city.country ASC
//    """)
//    fun searchCitiesByPrefixFts(prefix: String): Flow<List<CityEntity>>
}
