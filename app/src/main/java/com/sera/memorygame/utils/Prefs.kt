package com.sera.memorygame.utils

import android.content.Context
import android.content.SharedPreferences
import com.sera.memorygame.MemoryApplication

object Prefs {

    /**
     *
     */
    private fun getSharedPreference(): SharedPreferences = MemoryApplication.appContext.getSharedPreferences(MemoryApplication.appContext.packageName, Context.MODE_PRIVATE)

    /**
     *
     */
    private fun getEditor(): SharedPreferences.Editor = getSharedPreference().edit()

    /**
     *
     */
    fun setAssetVersion(version: String) {
        getEditor().putString("asset_version", version).apply()
    }

    /**
     *
     */
    fun getAssetVersion(): String = getSharedPreference().getString("asset_version", "0.0") ?: "0.0"
}