package com.sera.memorygame.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sera.memorygame.database.entity.UserEntity
import com.sera.memorygame.database.model.IObject

class SettingsViewModel(private val context: Context) : ViewModel() {

    /**
     *
     */
    private var currUser = MutableLiveData<UserEntity?>().apply {
        value = null
    }

    /**
     *
     */
    var getUser: MutableLiveData<UserEntity?>
        get() = currUser
        set(value) {
            currUser = value
        }

    /**
     *
     */
    fun getList(): ArrayList<IObject?> {
        return ArrayList<IObject?>().apply {
            this.add(getUser.value)
        }
    }

}