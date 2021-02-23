package com.sera.memorygame.ui

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.sera.memorygame.databinding.GameThemeRecyclerSingleViewBinding
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.model.GameThemeObject
import com.sera.memorygame.utils.Utils

class GameThemeViewHolder(val binding: ViewDataBinding, val callback: Handlers? = null) : RecyclerView.ViewHolder(binding.root) {

    /**
     *
     */
    fun bind(item: GameThemeObject) {
        with(binding as GameThemeRecyclerSingleViewBinding) {
            handlers = callback
            position = absoluteAdapterPosition
            titleTV.text = item.title
            Utils.getDrawableByReference(context = root.context, reference = item.iconReference)?.let {
                iconView.setImageDrawable(it)
            }
        }
    }

}