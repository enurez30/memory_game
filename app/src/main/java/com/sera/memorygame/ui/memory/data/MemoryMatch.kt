package com.sera.memorygame.ui.memory.data

import com.sera.memorygame.custom.MemoryCardView
import com.sera.memorygame.database.model.MemoryViewObject

data class MemoryMatch(
    val item: MemoryViewObject? = null,
    val memoryCardView: MemoryCardView? = null,
    val isHide: Boolean = false,
    val isShow: Boolean = false,
)