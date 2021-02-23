package com.sera.memorygame.ui.memory

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.sera.memorygame.model.MemoryViewObject
import com.sera.memorygame.databinding.MemoryRecyclerSingleViewBinding
import com.sera.memorygame.interfaces.Handlers

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