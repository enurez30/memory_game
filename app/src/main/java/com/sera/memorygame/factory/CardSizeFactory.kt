@file:Suppress("UNCHECKED_CAST")

package com.sera.memorygame.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sera.memorygame.providers.ResourcesProvider
import com.sera.memorygame.viewModel.CardsSizeChooseViewModel

class CardSizeFactory(private val resourcesProvider: ResourcesProvider, private val jsonReference: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsSizeChooseViewModel::class.java)) {
            return CardsSizeChooseViewModel(resourcesProvider = resourcesProvider, jsonReference = jsonReference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}