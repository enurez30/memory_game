package com.sera.memorygame.database.model

import com.sera.memorygame.utils.Constants

data class GameThemeObject(
    val title: String,
    val category: String,
    val iconReference: String,
    val jsonReference: String
) : IObject() {

    /**
     *
     */
    override fun getViewType(): Int = Constants.GAME_THEME_OBJECT_VIEW_TYPE
}
