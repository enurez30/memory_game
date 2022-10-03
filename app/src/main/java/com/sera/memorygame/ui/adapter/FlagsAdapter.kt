package com.sera.memorygame.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sera.memorygame.R
import com.sera.memorygame.database.model.FlagQuizSingleObject
import com.sera.memorygame.databinding.FlagQuizSingleViewBinding
import com.sera.memorygame.ui.view_holder.FlagQuizSingleViewHolder

class FlagsAdapter : ListAdapter<FlagQuizSingleObject, RecyclerView.ViewHolder>(BaseComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: FlagQuizSingleViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.flag_quiz_single_view,
            parent, false
        )
        return FlagQuizSingleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FlagQuizSingleViewHolder).bind(item = getItem(position) as FlagQuizSingleObject)
    }

    object BaseComparator : DiffUtil.ItemCallback<FlagQuizSingleObject>() {
        override fun areItemsTheSame(oldItem: FlagQuizSingleObject, newItem: FlagQuizSingleObject) =
            oldItem.flagName == newItem.flagName

        override fun areContentsTheSame(oldItem: FlagQuizSingleObject, newItem: FlagQuizSingleObject) =
            oldItem == newItem

    }

}