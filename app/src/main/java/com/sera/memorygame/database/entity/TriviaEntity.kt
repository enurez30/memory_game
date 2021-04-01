package com.sera.memorygame.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sera.memorygame.database.model.IObject
import com.sera.memorygame.utils.Constants
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

@Entity(
    tableName = "trivia_table",
    indices = [androidx.room.Index(value = arrayOf("id"))]
)
class TriviaEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "difficulty") var difficulty: String,
    @ColumnInfo(name = "question") var question: String,
    @ColumnInfo(name = "correct_answer") var correctAnswer: String,
    @ColumnInfo(name = "is_live") var isLive: Boolean,
    @ColumnInfo(name = "incorrect_answers") var incorrectAnswers: List<String> = ArrayList(),
    @ColumnInfo(name = "created") val created: Long
) : IObject(), Serializable {

    constructor() : this(
        id = UUID.randomUUID().toString(),
        category = "",
        type = "",
        difficulty = "",
        question = "",
        correctAnswer = "",
        isLive = true,
        incorrectAnswers = ArrayList(),
        created = System.currentTimeMillis()
    )

    /**
     *
     */
    override fun getViewType(): Int = Constants.TRIVIA_OBJECT_VIEW_TYPE

}