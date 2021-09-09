package com.sera.memorygame.database.repository

import com.sera.memorygame.database.dao.UserDao
import com.sera.memorygame.database.entity.UserEntity
import com.sera.memorygame.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val dao: UserDao) {

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