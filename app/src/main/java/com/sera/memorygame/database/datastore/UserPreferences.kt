package com.sera.memorygame.database.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(val context: Context) {


    private val Context.dataStore by preferencesDataStore(
        name = "app_preferences"
    )

    val appLanguage: Flow<String?>
        get() = context.dataStore.data.map { preferences ->
            preferences[KEY_APP_LANGUAGE]
        }

    suspend fun saveAppLanguage(langRef: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_APP_LANGUAGE] = langRef
        }
    }


    companion object {
        val KEY_APP_LANGUAGE = stringPreferencesKey("app_language")
    }

}