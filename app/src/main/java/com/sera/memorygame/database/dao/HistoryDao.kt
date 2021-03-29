package com.sera.memorygame.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.sera.memorygame.database.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class HistoryDao : BaseDao<HistoryEntity> {

    /**
     *
     */
    @Query("SELECT * FROM history_table")
    abstract fun getAllHistoryFlow(): Flow<List<HistoryEntity>>

    /**
     *
     */
    @Query("SELECT * FROM history_table WHERE type=:type")
    abstract fun getHistoryByType(type: String): HistoryEntity?
}