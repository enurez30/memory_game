package com.sera.memorygame.database.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TriviaObjectModel(
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("correct_answer")
    val correctAnswer: String? = null,
    @SerializedName("difficulty")
    val difficulty: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("question")
    val question: String? = null,
    @SerializedName("incorrect_answers")
    val answersList:List<String>? = null
): Parcelable
