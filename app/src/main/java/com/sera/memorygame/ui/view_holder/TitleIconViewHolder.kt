package com.sera.memorygame.ui.view_holder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.sera.memorygame.databinding.TitleIconRecyclerSingleViewBinding
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.model.TitleIconObject

class TitleIconViewHolder(val binding: ViewDataBinding, val callback: Handlers? = null) : RecyclerView.ViewHolder(binding.root) {

    /**
     *
     */
    fun bind(item: TitleIconObject) {
        with(binding as TitleIconRecyclerSingleViewBinding) {
            titleTV.text = item.title
        }
    }

}