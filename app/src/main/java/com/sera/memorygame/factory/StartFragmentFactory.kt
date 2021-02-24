@file:Suppress("UNCHECKED_CAST")

package com.sera.memorygame.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sera.memorygame.viewModel.StartViewModel

class StartFragmentFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartViewModel::class.java)) {
            return StartViewModel(context = context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}