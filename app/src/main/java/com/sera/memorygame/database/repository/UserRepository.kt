package com.sera.memorygame.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.sera.memorygame.database.AppDatabase
import com.sera.memorygame.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(context: Context) {

    private val dao = AppDatabase.getDataBase(context).userDao()

    /**
     *
     */
    fun createUser(userName: String = "Sergey"): UserEntity {
        return UserEntity().apply {
            this.userName = userName
            this.inSession = true
        }
    }

    /**
     *
     */
    fun persistUser(user: UserEntity) {
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
    fun getAllUsersLive(): LiveData<List<UserEntity>> = dao.getAllUsersLive()

    /**
     *
     */
    fun deleteAllUsers() {
        dao.deleteAllUsers()
    }
}