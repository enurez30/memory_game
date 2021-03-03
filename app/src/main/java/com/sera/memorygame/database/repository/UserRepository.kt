package com.sera.memorygame.database.repository

import androidx.lifecycle.LiveData
import com.sera.memorygame.MemoryApplication
import com.sera.memorygame.database.AppDatabase
import com.sera.memorygame.database.entity.UserEntity
import io.reactivex.Flowable

object UserRepository {

    private val dao = AppDatabase.getDataBase(MemoryApplication.appContext).userDao()

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
    fun deleteAllUsers(){
        dao.deleteAllUsers()
    }
}