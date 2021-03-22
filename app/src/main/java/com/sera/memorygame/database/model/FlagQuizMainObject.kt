package com.sera.memorygame.database.model

import com.sera.memorygame.utils.Constants
import java.io.Serializable

data class FlagQuizMainObject(
    val countryId: String,
    val flagReference: String,
    val options: ArrayList<FlagQuizSingleObject>
) : IObject(), Serializable {

    /**
     *
     */
    override fun getViewType(): Int = Constants.FLAG_QUIZ_MAIN_OBJECT_VIEW_TYPE
}