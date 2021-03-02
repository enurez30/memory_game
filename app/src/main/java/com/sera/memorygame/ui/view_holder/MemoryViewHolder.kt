package com.sera.memorygame.ui.view_holder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.sera.memorygame.databinding.MemoryRecyclerSingleViewBinding
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.model.MemoryViewObject

class MemoryViewHolder(val binding: ViewDataBinding, val callback: Handlers? = null) : RecyclerView.ViewHolder(binding.root) {

    /**
     *
     */
    fun bind(item: MemoryViewObject) {
        with(binding as MemoryRecyclerSingleViewBinding) {
            container.addView(item.memoryView!!)
        }
    }
}