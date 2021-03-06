package com.sera.memorygame.database.model

import com.sera.memorygame.utils.Constants
import java.io.Serializable

data class SizeViewObject(
    var title: String,
    var xAxis: Int,
    var yAxis: Int,
    var size: Int
) : IObject(), Serializable {

    /**
     *
     */
    override fun getViewType(): Int = Constants.SIZE_VIEW_OBJECT_VIEW_TYPE
}
