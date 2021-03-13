@file:Suppress("UNCHECKED_CAST")

package com.sera.memorygame.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sera.memorygame.database.repository.UserRepository
import com.sera.memorygame.viewModel.UserViewModel


class UserViewModelFactory (private val context: Context, private val repo: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repo = repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}