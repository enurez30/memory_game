package com.sera.memorygame.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sera.memorygame.database.AppDatabase
import com.sera.memorygame.database.dao.CoutryDao
import com.sera.memorygame.database.dao.HistoryDao
import com.sera.memorygame.database.dao.TriviaDao
import com.sera.memorygame.database.dao.UserDao
import com.sera.memorygame.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            Constants.APP_DATABAS_NAME
        )
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideCountryDao(appDatabase: AppDatabase): CoutryDao {
        return appDatabase.countryDao()
    }

    @Provides
    fun provideHistoryDao(appDatabase: AppDatabase): HistoryDao {
        return appDatabase.historyDao()
    }

    @Provides
    fun provideTriviaDao(appDatabase: AppDatabase): TriviaDao {
        return appDatabase.triviaDao()
    }
}