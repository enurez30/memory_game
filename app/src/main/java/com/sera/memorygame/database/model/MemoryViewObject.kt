package com.sera.memorygame.database.model

import com.sera.memorygame.R
import com.sera.memorygame.custom.MemoryCardView
import com.sera.memorygame.ui.memory.MemoryCardAnimationState
import com.sera.memorygame.utils.Constants

data class MemoryViewObject(
    val id: Int,
    val width: Float,
    val height: Float,
    val imageReference:String,
    val frontResource: Any? = R.drawable.bckg_squires,
    val backResource: Any? = R.drawable.bckg_squires,
    val onClick: (Pair<MemoryCardView, MemoryViewObject>) -> Unit,
    val onAnimationState:(state : MemoryCardAnimationState)->Unit
) : IObject() {

    /**
     *
     */
    override fun getViewType(): Int = Constants.MEMORY_VIEW_OBJECT_VIEW_TYPE

}
