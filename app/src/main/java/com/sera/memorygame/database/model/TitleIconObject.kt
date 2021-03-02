package com.sera.memorygame.database.model

import com.sera.memorygame.utils.Constants

data class TitleIconObject(
    var iconName: String,
    var title: String
) : IObject() {
    override fun getViewType(): Int = Constants.TITLE_ICON_OBJECT_VIEW_TYPE

}
