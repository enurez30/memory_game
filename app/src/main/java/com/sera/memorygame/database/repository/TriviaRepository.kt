@file:Suppress("UNCHECKED_CAST")

package com.sera.memorygame.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.sera.memorygame.database.AppDatabase
import com.sera.memorygame.database.dao.TriviaDao
import com.sera.memorygame.database.entity.TriviaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TriviaRepository @Inject constructor(val context: Context) {

    /**
     *
     */
    private val dao: TriviaDao by lazy {
        AppDatabase.getDataBase(context).triviaDao()
    }


    /**
     *
     */
    suspend fun insertMultiple(list: ArrayList<TriviaEntity>) {
        dao.insert(obj = list)
    }

    /**
     *
     */
    suspend fun deleteSingleObject(triviaEntity: TriviaEntity) {
        dao.delete(obj = triviaEntity)
    }

    /**
     *
     */
    suspend fun updateSingleObject(triviaEntity: TriviaEntity) {
        dao.update(obj = triviaEntity)
    }

    /**
     *
     */
    suspend fun getTriviaNotInRange(list: List<String>) = dao.getTriviaNotInRange(list = list)

    /**
     *
     */
    suspend fun getAllTriviaObjectsLive(): LiveData<ArrayList<TriviaEntity>> {
        return withContext(Dispatchers.IO){
            dao.getAllTriviaObjectsLive() as LiveData<ArrayList<TriviaEntity>>
        }
    }
}