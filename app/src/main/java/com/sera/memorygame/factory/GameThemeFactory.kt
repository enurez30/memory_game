@file:Suppress("UNCHECKED_CAST")

package com.sera.memorygame.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sera.memorygame.viewModel.GameThemeViewModel

class GameThemeFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameThemeViewModel::class.java)) {
            return GameThemeViewModel(context = context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}