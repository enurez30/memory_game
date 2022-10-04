package com.sera.memorygame.ui.memory.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sera.memorygame.R
import com.sera.memorygame.database.model.MemoryViewObject
import com.sera.memorygame.databinding.MemoryRecyclerSingleViewBinding
import com.sera.memorygame.ui.view_holder.MemoryViewHolder

class MemoryAdapter : ListAdapter<MemoryViewObject, MemoryViewHolder>(MemoryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryViewHolder {
        val binding: MemoryRecyclerSingleViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.memory_recycler_single_view,
            parent,
            false
        )
        return MemoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoryViewHolder, position: Int) {
        holder.bind(item = getItem(position))
    }


    object MemoryComparator : DiffUtil.ItemCallback<MemoryViewObject>() {
        override fun areItemsTheSame(oldItem: MemoryViewObject, newItem: MemoryViewObject) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MemoryViewObject, newItem: MemoryViewObject) =
            oldItem == newItem

    }

}