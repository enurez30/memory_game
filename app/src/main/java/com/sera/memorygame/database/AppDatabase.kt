package com.sera.memorygame.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sera.memorygame.database.entity.UserEntity

@Database(
    exportSchema = false,
    entities = [
        UserEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         *
         */
        fun getDataBase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "memory_database"
                )
                    .setJournalMode(JournalMode.TRUNCATE)
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}