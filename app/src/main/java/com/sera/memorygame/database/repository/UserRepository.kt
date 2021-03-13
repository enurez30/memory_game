package com.sera.memorygame.database.repository

import android.content.Context
import com.sera.memorygame.database.AppDatabase
import com.sera.memorygame.database.entity.UserEntity
import com.sera.memorygame.utils.Constants
import kotlinx.coroutines.flow.Flow

class UserRepository(context: Context) {

    private val dao = AppDatabase.getDataBase(context).userDao()

    /**
     *
     */
    fun createUser(userName: String = Constants.DEFAULT_PLAYER_NAME): UserEntity {
        return UserEntity().apply {
            this.userName = userName
            this.inSession = true
        }
    }

    /**
     *
     */
    suspend fun persistUser(user: UserEntity) {
        dao.insert(obj = user)
    }

    /**
     *
     */
    fun getAllUsersFlow(): Flow<List<UserEntity>> = dao.getAllUsersFlow()

    /**
     *
     */
    fun getUserInSession(): Flow<UserEntity?> = dao.getUserInSession()

    /**
     *
     */
    suspend fun updateUser(user: UserEntity) {
        dao.update(obj = user)
    }

    /**
     *
     */
    fun deleteAllUsers() {
        dao.deleteAllUsers()
    }
}