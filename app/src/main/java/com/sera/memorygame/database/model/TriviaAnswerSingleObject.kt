package com.sera.memorygame.database.model

import com.sera.memorygame.utils.Constants

data class TriviaAnswerSingleObject(
    val answer: String,
    val isRight: Boolean = false,
    var animate: Boolean = false
) : IObject() {
    /**
     *
     */
    override fun getViewType(): Int = Constants.TRIVIA_ANSWER_SINGLE_OBJECT_VIEW_TYPE
}
