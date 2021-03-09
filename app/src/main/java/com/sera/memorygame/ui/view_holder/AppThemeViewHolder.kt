package com.sera.memorygame.ui.view_holder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.sera.memorygame.database.model.AppThemeObject
import com.sera.memorygame.databinding.AppThemeRecyclerSingleViewBinding
import com.sera.memorygame.interfaces.Handlers

class AppThemeViewHolder(val binding: ViewDataBinding, val callback: Handlers? = null) : RecyclerView.ViewHolder(binding.root) {

    /**
     *
     */
    fun bind(item: AppThemeObject) {
        with(binding as AppThemeRecyclerSingleViewBinding) {
            handlers = callback
            position = absoluteAdapterPosition

            themeName.text = item.title
            circleView.setColors(mainColor = item.mainColor, secondaryColor = item.secondaryColor)
        }
    }

}