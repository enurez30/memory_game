package com.sera.memorygame.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "user_table",
    indices = [androidx.room.Index(value = arrayOf("user_id"))]
)

class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id") var userId: String,
    @ColumnInfo(name = "user_name") var userName: String,
    @ColumnInfo(name = "avatar") var avatar: String,
    @ColumnInfo(name = "last_time_log_in") val lastTimeLoggedIn: Long,
    @ColumnInfo(name = "created") val created: Long
) {
    constructor() : this(
        userId = UUID.randomUUID().toString(),
        userName = "",
        avatar = "",
        lastTimeLoggedIn = 0L,
        created = System.currentTimeMillis()
    )
}