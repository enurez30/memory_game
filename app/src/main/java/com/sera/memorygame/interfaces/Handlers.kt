package com.sera.memorygame.interfaces

import android.view.View

interface Handlers {
    fun onHandlerClicked(view: View)

    fun onHandleClickedWithPosition(view: View, position: Int)

    fun onLongClicked(view: View, position: Int?): Boolean
}