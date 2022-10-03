package com.sera.memorygame.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Delete
    suspend fun delete(obj: T)

    @Delete
    suspend fun delete(obj: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: List<T>): List<Long>

    @Update
    suspend fun update(obj: T)

    @Update
    suspend fun update(obj: List<T>)

//    @Transaction
//    fun insertOrUpdate(obj: T) {
//        val id = insert(obj)
//        if (id == -1L) update(obj)
//    }
//
//    @Transaction
//    fun insertOrUpdate(objList: List<T>) {
//        val insertResult = insert(objList)
//        val updateList = mutableListOf<T>()
//
//        for (i in insertResult.indices) {
//            if (insertResult[i] == -1L) updateList.add(objList[i])
//        }
//
//        if (updateList.isNotEmpty()) update(updateList)
//    }

}