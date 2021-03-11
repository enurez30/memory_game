@file:Suppress("UNCHECKED_CAST")

package com.sera.memorygame.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sera.memorygame.database.repository.UserRepository
import com.sera.memorygame.viewModel.SettingsViewModel

class SettingsFactory(private val context: Context, private val repo: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(context = context, repo = repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}