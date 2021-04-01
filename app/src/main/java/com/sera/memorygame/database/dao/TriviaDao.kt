package com.sera.memorygame.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.sera.memorygame.database.entity.TriviaEntity

@Dao
abstract class TriviaDao : BaseDao<TriviaEntity> {


    /**
     *
     */
    @Query("SELECT * FROM trivia_table WHERE id NOT IN (:list) AND is_live=1")
    abstract suspend fun getTriviaNotInRange(list: List<String>): List<TriviaEntity>

    /**
     *
     */
    @Query("SELECT * FROM trivia_table WHERE is_live=1")
    abstract fun getAllTriviaObjectsLive() : LiveData<List<TriviaEntity>>
}