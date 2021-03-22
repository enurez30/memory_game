package com.sera.memorygame.database.model

import com.sera.memorygame.utils.Constants
import java.io.Serializable

data class FlagQuizSingleObject(
    val flagName: String,
    val isRight: Boolean = false,
    var animate: Boolean = false
) : IObject(), Serializable {
    override fun getViewType(): Int = Constants.FLAG_QUIZ_SINGLE_OBJECT_VIEW_TYPE
}
