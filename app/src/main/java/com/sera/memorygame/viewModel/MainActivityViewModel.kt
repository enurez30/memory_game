package com.sera.memorygame.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sera.memorygame.database.entity.UserEntity
import com.sera.memorygame.database.repository.UserRepository

class MainActivityViewModel(private val context: Context, private val repo: UserRepository) : ViewModel() {

    /**
     *
     */
    private var user = MutableLiveData<UserEntity?>().apply {
        value = null
    }

    /**
     *
     */
    var getUser: MutableLiveData<UserEntity?>
        get() = user
        set(value) {
            user = value
        }

    fun getUsersFromDB(): LiveData<List<UserEntity>> = repo.getAllUsersLive()

    fun getName(): String = getUser.value?.userName?:"hhhhhhhhh"
}