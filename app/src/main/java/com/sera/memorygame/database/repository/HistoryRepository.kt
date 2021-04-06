package com.sera.memorygame.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.sera.memorygame.database.AppDatabase
import com.sera.memorygame.database.dao.HistoryDao
import com.sera.memorygame.database.entity.HistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryRepository @Inject constructor(val context: Context) {

    /**
     *
     */
    private val dao: HistoryDao by lazy {
        AppDatabase.getDataBase(context).historyDao()
    }

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
    suspend fun getHistoryEntityByType(type: String): HistoryEntity? {
        return withContext(Dispatchers.IO) {
            dao.getHistoryByType(type = type)
        }
    }

    /**
     *
     */
    fun getHistoryByTypeLive(type: String): LiveData<HistoryEntity?> = dao.getHistoryByTypeLive(type = type)
}