package com.sera.memorygame.database.model

import com.sera.memorygame.R
import com.sera.memorygame.custom.MemoryCardView
import com.sera.memorygame.utils.Constants

data class MemoryViewObject(
    val id: Int,
    val width: Float,
    val height: Float,
    val frontResource: Any? = R.drawable.bckg_squires,
    val backResource: Any? = R.drawable.bckg_squires,
    var memoryView: MemoryCardView? = null
) : IObject() {

    /**
     *
     */
    override fun getViewType(): Int = Constants.MEMORY_VIEW_OBJECT_VIEW_TYPE

}
