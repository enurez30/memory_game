package com.sera.memorygame.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sera.memorygame.database.dao.CoutryDao
import com.sera.memorygame.database.dao.HistoryDao
import com.sera.memorygame.database.dao.TriviaDao
import com.sera.memorygame.database.dao.UserDao
import com.sera.memorygame.database.entity.CountryEntity
import com.sera.memorygame.database.entity.HistoryEntity
import com.sera.memorygame.database.entity.TriviaEntity
import com.sera.memorygame.database.entity.UserEntity

@Database(
    exportSchema = false,
    entities = [
        UserEntity::class,
        CountryEntity::class,
        HistoryEntity::class,
        TriviaEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun countryDao(): CoutryDao
    abstract fun historyDao(): HistoryDao
    abstract fun triviaDao(): TriviaDao

//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        /**
//         *
//         */
//        fun getDataBase(context: Context): AppDatabase {
//            val tempInstance = INSTANCE
//            if (tempInstance != null) {
//                return tempInstance
//            }
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    Constants.APP_DATABAS_NAME
//                )
//                    .setJournalMode(JournalMode.TRUNCATE)
//                    .allowMainThreadQueries()
//                    .build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }

}