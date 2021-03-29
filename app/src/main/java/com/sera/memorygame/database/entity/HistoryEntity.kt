package com.sera.memorygame.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sera.memorygame.database.model.IObject
import com.sera.memorygame.database.model.ScoreObject
import com.sera.memorygame.utils.Constants
import java.util.*
import kotlin.collections.ArrayList


@Entity(
    tableName = "history_table",
    indices = [androidx.room.Index(value = arrayOf("id"))]
)
class HistoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "score") var score: ScoreObject,
    @ColumnInfo(name = "country_ids") var countryIds: List<String> = ArrayList(),
    @ColumnInfo(name = "created") var created: Long
) : IObject() {

    constructor() : this(
        id = UUID.randomUUID().toString(),
        category = "",
        type = "",
        score = ScoreObject(),
        countryIds = ArrayList(),
        created = System.currentTimeMillis()
    )

    /**
     *
     */
    override fun getViewType(): Int = Constants.HISTORY_OBJECT_VIEW_TYPE


}