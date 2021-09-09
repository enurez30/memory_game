@file:Suppress("UNCHECKED_CAST")

package com.sera.memorygame.database.repository

import androidx.lifecycle.LiveData
import com.sera.memorygame.database.dao.TriviaDao
import com.sera.memorygame.database.entity.TriviaEntity
import com.sera.memorygame.network.IServiceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.http.QueryMap
import javax.inject.Inject

class TriviaRepository @Inject constructor(private val dao: TriviaDao, private val service: IServiceHelper) {

    /**
     *
     */
    suspend fun insertMultiple(list: List<TriviaEntity>) {
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
    fun getTriviaNotInRangeLive(list: List<String>): LiveData<List<TriviaEntity>> = dao.getTriviaNotInRangeLive(list = list)

    /**
     *
     */
    fun getTriviaNotInRangeFlow(list: List<String>): Flow<List<TriviaEntity>> = dao.getTriviaNotInRangeFlow(list = list)

    /**
     *
     */
    suspend fun getAllTriviaObjectsLive(): LiveData<ArrayList<TriviaEntity>> {
        return withContext(Dispatchers.IO) {
            dao.getAllTriviaObjectsLive() as LiveData<ArrayList<TriviaEntity>>
        }
    }

    /**
     *
     */
    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            dao.deleteAll()
        }
    }

    // --- Network --- //

    suspend fun getTriviaCategories() = service.getTriviaCategories()

    suspend fun getTriviaQuestionsFlow(@QueryMap options: Map<String, String>) = service.getTriviaQuestionsFlow(options = options)
}