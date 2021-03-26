package com.sera.memorygame.ui.view_holder

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.sera.memorygame.R
import com.sera.memorygame.database.model.SizeViewObject
import com.sera.memorygame.databinding.SizeRecyclerSingleViewBinding
import com.sera.memorygame.interfaces.Handlers

class SizeViewHolder(val binding: ViewDataBinding, val callback: Handlers? = null) : RecyclerView.ViewHolder(binding.root) {

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    fun bind(item: SizeViewObject) {
        with(binding as SizeRecyclerSingleViewBinding) {
            handlers = callback
            position = absoluteAdapterPosition
            titleTV.text = StringBuilder().apply {
                this.append("${item.xAxis} X ${item.yAxis}")
                this.append("\n")
//                this.append("${item.size} Cards")
                this.append(root.context.getString(R.string.cards, item.size))
            }.toString()
        }
    }

}