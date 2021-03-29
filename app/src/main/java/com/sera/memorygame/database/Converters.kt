package com.sera.memorygame.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.sera.memorygame.database.model.ScoreObject
import java.lang.reflect.Type

class Converters {

    /**
     *
     */
    private fun getGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    /**
     * String
     */
    @TypeConverter
    fun fromListStringToString(value: List<String?>?): String? {
        if (value == null) {
            return null
        }
        val gson = getGson()
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun fromStringToListString(value: String?): List<String?>? {
        if (value == null) {
            return null
        }
        val gson = getGson()
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson(value, type)
    }

    /**
     * Score
     */
    @TypeConverter
    fun fromScoreObjectToString(score: ScoreObject?): String? {
        if (score == null) {
            return null
        }
        val gson = getGson()
        val type: Type = object : TypeToken<ScoreObject?>() {}.type
        return gson.toJson(score, type)
    }

    @TypeConverter
    fun fromStringToScoreObject(score: String?): ScoreObject? {
        if (score == null) {
            return null
        }
        val gson = getGson()
        val type: Type = object : TypeToken<ScoreObject?>() {}.type
        return gson.fromJson(score, type)
    }


}