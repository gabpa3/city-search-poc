package com.gabcode.citysearchpoc.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "city",
    indices = [Index(value = ["name", "country"])]
)
data class CityEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val name: String,
    val country: String,
    val longitude: Double,
    val latitude: Double,
    val isFavorite: Boolean = false
)

@Entity(tableName = "city_fts")
@Fts4(contentEntity = CityEntity::class, tokenizer = "unicode61")
data class CityFtsEntity(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val id: Int,
    val name: String,
    val country: String
)
