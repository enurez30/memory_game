package com.sera.memorygame.database.model

data class LanguageObject(val iconRefrerence: String, val languageRefrence: String, val name: String) : IObject() {

    /**
     *
     */
    override fun getViewType(): Int = -1

}
