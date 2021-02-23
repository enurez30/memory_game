package com.sera.memorygame.model

import com.sera.memorygame.Constants

data class TitleIconObject(
    var iconName: String,
    var title: String
) : IObject() {
    override fun getViewType(): Int = Constants.TITLE_ICON_OBJECT_VIEW_TYPE

}
