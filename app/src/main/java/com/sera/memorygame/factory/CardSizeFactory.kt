package com.sera.memorygame.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sera.memorygame.viewModel.CardsSizeChooseViewModel

class CardSizeFactory(private val context: Context, private val jsonReference: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsSizeChooseViewModel::class.java)) {
            return CardsSizeChooseViewModel(context = context, jsonReference = jsonReference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}