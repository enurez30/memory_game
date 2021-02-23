package com.sera.memorygame.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sera.memorygame.model.SizeViewObject
import com.sera.memorygame.viewModel.MemoryViewModel

class MemoryViewModelFactory(private val context: Context, private val jsonRef: String, private val sizeViewObject: SizeViewObject) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemoryViewModel::class.java)) {
            return MemoryViewModel(context = context, jsonRef = jsonRef, sizeViewObject = sizeViewObject) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}