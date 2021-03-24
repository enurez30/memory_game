package com.sera.memorygame.ui.view_holder

import android.view.View
import com.sera.memorygame.database.model.LanguageObject
import com.sera.memorygame.databinding.SimpleIobjectAutocompleteLayoutBinding
import com.sera.memorygame.interfaces.Handlers
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
                ResourcesProvider(context = root.context).getResurceFromRaw(fName = it.iconRefrerence).let { resId ->
                    icon.setImageResource(resId)
                }
                nameTV.text = it.name
            }

        }
    }
}