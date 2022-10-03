package com.sera.memorygame.database.repository

import androidx.lifecycle.LiveData
import com.sera.memorygame.database.dao.HistoryDao
import com.sera.memorygame.database.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryRepository @Inject constructor(private val dao: HistoryDao) {

    /**
     *
     */
    fun getEmptyHistoryEntity(): HistoryEntity = HistoryEntity()

    /**
     *
     */
    suspend fun delete(historyEntity: HistoryEntity) {
        dao.delete(obj = historyEntity)
    }

    /**
     *
     */
    suspend fun persistHistoryEntity(historyEntity: HistoryEntity) {
        dao.insert(obj = historyEntity)
    }

    /**
     *
     */
    suspend fun updateHistoryEntity(historyEntity: HistoryEntity) {
        dao.update(obj = historyEntity)
    }

    /**
     *
     */
    fun getAllHistoryFlow(): Flow<List<HistoryEntity>> = dao.getAllHistoryFlow()

    /**
     *
     */
    suspend fun getHistoryEntityByType(type: String): HistoryEntity? = dao.getHistoryByType(type = type)

    /**
     *
     */
    fun getHistoryByTypeLive(type: String): LiveData<HistoryEntity?> = dao.getHistoryByTypeLive(type = type)

    /**
     *
     */
    fun getHistoryByTypeFlow(type: String): Flow<HistoryEntity?> = dao.getHistoryByTypeFlow(type = type)
}