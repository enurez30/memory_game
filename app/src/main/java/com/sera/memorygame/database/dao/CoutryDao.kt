package com.sera.memorygame.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.sera.memorygame.database.entity.CountryEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CoutryDao : BaseDao<CountryEntity> {

    /**
     *
     */
    @Query("SELECT * FROM country_table")
    abstract fun getAllCountries(): Flow<List<CountryEntity>>

    /**
     *
     */
    @Query("SELECT * FROM country_table")
    abstract suspend fun getAllCountriesOnly(): List<CountryEntity>
}