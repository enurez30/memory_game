@file:Suppress("UNCHECKED_CAST")

package com.sera.memorygame.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sera.memorygame.database.model.SizeViewObject
import com.sera.memorygame.providers.ResourcesProvider
import com.sera.memorygame.viewModel.MemoryViewModel

class MemoryViewModelFactory(private val jsonRef: String, private val sizeViewObject: SizeViewObject, private val provider: ResourcesProvider) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemoryViewModel::class.java)) {
            return MemoryViewModel(jsonRef = jsonRef, sizeViewObject = sizeViewObject, resourcesProvider = provider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}