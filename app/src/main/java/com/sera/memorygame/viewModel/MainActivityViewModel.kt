package com.sera.memorygame.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sera.memorygame.database.entity.UserEntity
import com.sera.memorygame.database.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivityViewModel(private val context: Context, private val repo: UserRepository) : ViewModel() {

    /**
     *
     */
    init {
        fetchUser()
    }

    /**
     *
     */
    private val sessionUser = MutableStateFlow<UserEntity?>(null)

    /**
     *
     */
    fun getName(): String = sessionUser.value?.userName ?: "hhhhhhhhh"

    /**
     *
     */
    fun getUserInSession(): StateFlow<UserEntity?> = sessionUser

    /**
     *
     */
    private fun fetchUser() {
        viewModelScope.launch {
            repo.getUserInSession()
                .catch {
                    println("error")
                }
                .collect {
                    sessionUser.value = it
                }
        }
    }

}