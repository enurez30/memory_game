package com.sera.memorygame.ui.view_holder

import android.view.View
import com.sera.memorygame.database.model.LanguageObject
import com.sera.memorygame.databinding.SimpleIobjectAutocompleteLayoutBinding
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.network.model.TriviaCategoryModel
import com.sera.memorygame.providers.ResourcesProvider

class SimpleIObjectViewHolder<T>(val binding: SimpleIobjectAutocompleteLayoutBinding, val callback: Handlers?) {
    var view: View = binding.root

    /**
     *
     */
    fun bind(item: T, itemPosition: Int) {
        with(binding) {
            handlers = callback
            position = itemPosition

            (item as? LanguageObject?)?.let {
                with(ResourcesProvider(context = root.context)){
                    it.iconRefrerence.getResurceFromRaw.let{resId ->
                        icon.setImageResource(resId)
                    }
                }
                nameTV.text = it.name
            }

            (item as? TriviaCategoryModel?)?.let {
                icon.visibility = View.GONE
                nameTV.text = it.name
            }
        }
    }
}