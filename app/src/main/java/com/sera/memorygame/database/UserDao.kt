package com.sera.memorygame.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.sera.memorygame.database.entity.UserEntity
import io.reactivex.Flowable

@Dao
abstract class UserDao : BaseDao<UserEntity> {

    /**
     * Get a user by id.
     * @return the user from the table with a specific id.
     */
    @Query("SELECT * FROM user_table WHERE user_id = :id")
    protected abstract fun getUserByIdFlowable(id: String): Flowable<UserEntity>

    /**
     * Get all users.
     * @return the users from the table .
     */
    @Query("SELECT * FROM user_table")
    protected abstract fun getAllUsersFlowable(): Flowable<List<UserEntity>>

    /**
     *
     */
    fun getUserByIdDistinctFlowable(id: String): Flowable<UserEntity> = getUserByIdFlowable(id).distinctUntilChanged()

    /**
     *
     */
    fun getAllUsersDistinctFlowable(): Flowable<List<UserEntity>> = getAllUsersFlowable().distinctUntilChanged()

    /**
     *
     */
    @Query("SELECT * FROM user_table")
    abstract fun getAllUsersLive(): LiveData<List<UserEntity>>

    /**
     * Delete all users.
     */
    @Query("DELETE FROM user_table")
    abstract fun deleteAllUsers()
}