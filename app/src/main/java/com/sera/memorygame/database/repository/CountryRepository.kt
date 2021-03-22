package com.sera.memorygame.database.repository

import android.content.Context
import com.sera.memorygame.database.AppDatabase
import com.sera.memorygame.database.dao.CoutryDao
import com.sera.memorygame.database.entity.CountryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CountryRepository @Inject constructor(val context: Context) {

    private val dao: CoutryDao by lazy {
        AppDatabase.getDataBase(context).countryDao()
    }

    /**
     *
     */
    fun getEmptyCountryEntity(): CountryEntity = CountryEntity()

    /**
     *
     */
    suspend fun insertMultipleContryEntities(arr: ArrayList<CountryEntity>) {
        dao.insert(obj = arr)
    }

    /**
     *
     */
    suspend fun updateCountryEntity(entity: CountryEntity) {
        dao.update(obj = entity)
    }

    /**
     *
     */
    fun getAllCountries(): Flow<List<CountryEntity>> = dao.getAllCountries()

    /**
     *
     */
    suspend fun getAllCountriesOnly(): List<CountryEntity> = dao.getAllCountriesOnly()
}