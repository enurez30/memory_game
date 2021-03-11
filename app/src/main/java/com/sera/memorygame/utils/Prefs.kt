package com.sera.memorygame.utils

import android.content.Context
import android.content.SharedPreferences
import com.sera.memorygame.MemoryApplication
import com.sera.memorygame.R

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

    /**
     *
     */
    fun setTheme(themeRes: Int) {
        getEditor().putInt("theme", themeRes).apply()
    }

    /**
     *
     */
    fun getTheme(): Int = getSharedPreference().getInt("theme", R.style.Theme_Pink)

    /**
     *
     */
    fun setThemeName(name: String) = getEditor().putString("theme_name", name).apply()

    /**
     *
     */
    fun getThemeName(): String = getSharedPreference().getString("theme_name", "Pink") ?: "Pink"
}