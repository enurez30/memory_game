package com.sera.memorygame.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.sera.memorygame.database.AppDatabase
import com.sera.memorygame.database.entity.UserEntity
import io.reactivex.Flowable

class UserRepository(context: Context) {

    private val dao = AppDatabase.getDataBase(context).userDao()

    /**
     *
     */
    fun createUser(userName: String = "Sergey"): UserEntity {
        return UserEntity().apply {
            this.userName = userName
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
    fun getAllUsers(): Flowable<List<UserEntity>> = dao.getAllUsersDistinctFlowable()

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