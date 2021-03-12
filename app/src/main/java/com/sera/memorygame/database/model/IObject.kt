package com.sera.memorygame.database.model

abstract class IObject {

    abstract fun getViewType(): Int

    open fun isItemSelected(): Boolean = false

    open fun getAvatarReference(): String = ""
}