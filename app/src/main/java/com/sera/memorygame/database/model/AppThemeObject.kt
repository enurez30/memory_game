package com.sera.memorygame.database.model

import com.sera.memorygame.utils.Constants

data class AppThemeObject(
    val title: String,
    val mainColor: Int,
    val secondaryColor: Int,
    val themeRes:Int
) : IObject() {

    /**
     *
     */
    override fun getViewType(): Int = Constants.APP_THEME_OBJECT_VIEW_TYPE

}
