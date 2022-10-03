package com.sera.memorygame.ui.view_holder

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.sera.memorygame.R
import com.sera.memorygame.database.model.FlagQuizSingleObject
import com.sera.memorygame.databinding.FlagQuizSingleViewBinding
import com.sera.memorygame.extentions.themeColor
import com.sera.memorygame.utils.AnimationHelper

class FlagQuizSingleViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    /**
     *
     */
    fun bind(item: FlagQuizSingleObject) {
        with(binding as FlagQuizSingleViewBinding) {
//            handlers = callback
//            position = absoluteAdapterPosition

            nameTV.text = item.flagName

            val mainColor = root.context.themeColor(attrRes = R.attr.colorSecondary)
            if (item.animate) {
                if (item.isRight) {
                    AnimationHelper.animateColorBackground(target = mainView, fromColor = mainColor, toColor = ContextCompat.getColor(root.context, R.color.green_700), duration = 250L)
                } else {
                    AnimationHelper.animateColorBackground(target = mainView, fromColor = mainColor, toColor = ContextCompat.getColor(root.context, R.color.red_700Dark), duration = 250L)
                }
            } else {
                mainView.backgroundTintList = ColorStateList.valueOf(mainColor)
            }

            mainView.setOnClickListener { item.onClick(item) }
        }
    }

}