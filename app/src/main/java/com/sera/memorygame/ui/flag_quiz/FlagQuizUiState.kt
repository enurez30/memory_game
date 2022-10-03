package com.sera.memorygame.ui.flag_quiz

import com.sera.memorygame.database.model.FlagQuizMainObject

data class FlagQuizUiState(
    val flagQuizObject: FlagQuizMainObject? = null,
    val correctCountries: Int = 0,
    val wrongCountries: Int = 0,
    val totalCountries: Int = 0,
    val isGoNext: Boolean = false,
    val forceExit: Boolean = false,
)

enum class KeyEnum(val value: String) {
    DEFAULT(""),
    CORRECT("correct"),
    WRONG("wrong")
}