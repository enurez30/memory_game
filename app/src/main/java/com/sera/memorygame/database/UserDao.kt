package com.sera.memorygame.database

import androidx.room.Dao
import androidx.room.Query
import com.sera.memorygame.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao : BaseDao<UserEntity> {


    /**
     *
     */
    @Query("SELECT * FROM user_table")
    abstract fun getAllUsersFlow(): Flow<List<UserEntity>>

    /**
     *
     */
    @Query("SELECT * FROM user_table WHERE is_session = 1")
    abstract fun getUserInSession(): Flow<UserEntity?>

    /**
     * Delete all users.
     */
    @Query("DELETE FROM user_table")
    abstract fun deleteAllUsers()
}