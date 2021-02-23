package com.sera.memorygame.model

import com.sera.memorygame.Constants
import com.sera.memorygame.R
import com.sera.memorygame.view.MemoryCardView

data class MemoryViewObject(
    val id: Int,
    val width: Float,
    val height: Float,
    val frontResource: Any? = R.drawable.dog_bck,
    val backResource: Any? = R.drawable.heart_bckg,
    var memoryView: MemoryCardView? = null
) : IObject() {

    /**
     *
     */
    override fun getViewType(): Int = Constants.MEMORY_VIEW_OBJECT_VIEW_TYPE

}
