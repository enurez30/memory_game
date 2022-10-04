package com.sera.memorygame.ui.view_holder

import androidx.recyclerview.widget.RecyclerView
import com.sera.memorygame.database.model.MemoryViewObject
import com.sera.memorygame.databinding.MemoryRecyclerSingleViewBinding

class MemoryViewHolder(val binding: MemoryRecyclerSingleViewBinding) : RecyclerView.ViewHolder(binding.root) {

    /**
     *
     */
    fun bind(item: MemoryViewObject) {
        with(binding) {
            memoryView.generate(item = item)
//            if (item.revealCard) {
//                memoryView.showCard()
//            } else {
//                memoryView.reset()
//            }
        }
    }
}