package com.sera.memorygame.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sera.memorygame.database.model.IObject
import com.sera.memorygame.utils.Constants
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
    @ColumnInfo(name = "is_session") var inSession: Boolean,
    @ColumnInfo(name = "last_time_log_in") val lastTimeLoggedIn: Long,
    @ColumnInfo(name = "created") val created: Long
) : IObject() {
    constructor() : this(
        userId = UUID.randomUUID().toString(),
        userName = "",
        avatar = "",
        inSession = false,
        lastTimeLoggedIn = 0L,
        created = System.currentTimeMillis()
    )

    override fun getViewType(): Int = Constants.USER_ENTITY_VIEW_TYPE
}