package com.sera.memorygame.model

import com.sera.memorygame.Constants

data class GameThemeObject(
    val title: String,
    val iconReference: String,
    val jsonReference: String
) : IObject() {

    /**
     *
     */
    override fun getViewType(): Int = Constants.GAME_THEME_OBJECT_VIEW_TYPE
}
