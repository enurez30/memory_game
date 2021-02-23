package com.sera.memorygame.model

abstract class IObject {

    abstract fun getViewType(): Int

    open fun isItemSelected(): Boolean = false
}