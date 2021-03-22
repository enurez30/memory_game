package com.sera.memorygame.ui.view_holder

import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.sera.memorygame.R
import com.sera.memorygame.database.model.FlagQuizSingleObject
import com.sera.memorygame.databinding.FlagQuizSingleViewBinding
import com.sera.memorygame.extentions.themeColor
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.utils.AnimationHelper

class FlagQuizSingleViewHolder(val binding: ViewDataBinding, val callback: Handlers? = null) : RecyclerView.ViewHolder(binding.root) {

    /**
     *
     */
    fun bind(item: FlagQuizSingleObject) {
        with(binding as FlagQuizSingleViewBinding) {
            handlers = callback
            position = absoluteAdapterPosition

            nameTV.text = item.flagName
            if (item.animate) {
                val fromColor = root.context.themeColor(attrRes = R.attr.colorSecondary)
                if (item.isRight) {
                    AnimationHelper.animateColorBackground(target = mainView, fromColor = fromColor, toColor = ContextCompat.getColor(root.context, R.color.green_700), duration = 250L)
                } else {
                    AnimationHelper.animateColorBackground(target = mainView, fromColor = fromColor, toColor = ContextCompat.getColor(root.context, R.color.red_700), duration = 250L)
                }
            }
        }
    }

}