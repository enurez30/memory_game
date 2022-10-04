package com.sera.memorygame.ui.memory

import com.sera.memorygame.database.model.MemoryViewObject
import com.sera.memorygame.database.model.SizeViewObject
import com.sera.memorygame.ui.memory.data.MemoryMatch

data class MemoryUiState(
    val sizeObject: SizeViewObject? = null,
    val jsonRef: String = "",
    val cards: List<MemoryViewObject> = listOf(),
    val matchPair: Pair<MemoryMatch?, MemoryMatch?>? = null,
    val matchedPairsValue: Int = 0
)

enum class MemoryCardAnimationState {
    ANIMATION_IN_PROGRESS,
    ANIMATION_ENDED
}