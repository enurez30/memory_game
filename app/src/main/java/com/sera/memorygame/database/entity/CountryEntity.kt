package com.sera.memorygame.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sera.memorygame.database.model.IObject
import com.sera.memorygame.utils.Constants
import java.util.*

@Entity(
    tableName = "country_table",
    indices = [androidx.room.Index(value = arrayOf("id"))]
)
class CountryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "code") var code: String,
    @ColumnInfo(name = "name_reference") var nameReference: String,
    @ColumnInfo(name = "image_reference") var imageReference: String,
    @ColumnInfo(name = "created") val created: Long
) : IObject() {

    constructor() : this(
        id = UUID.randomUUID().toString(),
        name = "",
        code = "",
        nameReference = "",
        imageReference = "",
        created = System.currentTimeMillis()
    )

    /**
     *
     */
    override fun getViewType(): Int = Constants.COUNTRY_OBJECT_VIEW_TYPE

}