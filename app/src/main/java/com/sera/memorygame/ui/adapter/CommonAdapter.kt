package com.sera.memorygame.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sera.memorygame.R
import com.sera.memorygame.database.model.*
import com.sera.memorygame.databinding.*
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.ui.view_holder.*
import com.sera.memorygame.utils.Constants

class CommonAdapter(handlers: Handlers? = null) : BaseRecyclerViewAdapter(callback = handlers) {

    /**
     *
     */
    override fun setViewHolder(parent: ViewGroup?, viewType: Int, callback: Handlers?): RecyclerView.ViewHolder {
        return when (viewType) {
            Constants.MEMORY_VIEW_OBJECT_VIEW_TYPE -> {
                val binding: MemoryRecyclerSingleViewBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent?.context),
                    R.layout.memory_recycler_single_view,
                    parent,
                    false
                )
                MemoryViewHolder(binding, callback)
            }
            Constants.SIZE_VIEW_OBJECT_VIEW_TYPE -> {
                val binding: SizeRecyclerSingleViewBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent?.context),
                    R.layout.size_recycler_single_view,
                    parent,
                    false
                )
                SizeViewHolder(binding, callback)
            }
            Constants.GAME_THEME_OBJECT_VIEW_TYPE -> {
                val binding: GameThemeRecyclerSingleViewBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent?.context),
                    R.layout.game_theme_recycler_single_view,
                    parent,
                    false
                )
                GameThemeViewHolder(binding, callback)
            }
            Constants.TITLE_ICON_OBJECT_VIEW_TYPE -> {
                val binding: TitleIconRecyclerSingleViewBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent?.context),
                    R.layout.title_icon_recycler_single_view,
                    parent,
                    false
                )
                TitleIconViewHolder(binding, callback)
            }
            Constants.APP_THEME_OBJECT_VIEW_TYPE -> {
                val binding: AppThemeRecyclerSingleViewBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent?.context),
                    R.layout.app_theme_recycler_single_view,
                    parent, false
                )
                AppThemeViewHolder(binding, callback)
            }
//            Constants.FLAG_QUIZ_SINGLE_OBJECT_VIEW_TYPE -> {
//                val binding: FlagQuizSingleViewBinding = DataBindingUtil.inflate(
//                    LayoutInflater.from(parent?.context),
//                    R.layout.flag_quiz_single_view,
//                    parent, false
//                )
//                FlagQuizSingleViewHolder(binding, callback)
//            }
            Constants.TRIVIA_ANSWER_SINGLE_OBJECT_VIEW_TYPE -> {
                val binding: FlagQuizSingleViewBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent?.context),
                    R.layout.flag_quiz_single_view,
                    parent, false
                )
                TriviaAnswerSingleViewHolder(binding, callback)
            }
            else -> {
                val binding: MemoryRecyclerSingleViewBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent?.context),
                    R.layout.memory_recycler_single_view,
                    parent,
                    false
                )
                MemoryViewHolder(binding, callback)
            }
        }

    }

    /**
     *
     */
    override fun onBindData(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is MemoryViewHolder -> holder.bind(item = items[position] as MemoryViewObject)
            is SizeViewHolder -> holder.bind(item = items[position] as SizeViewObject)
            is TitleIconViewHolder -> holder.bind(item = items[position] as TitleIconObject)
            is GameThemeViewHolder -> holder.bind(item = items[position] as GameThemeObject)
            is AppThemeViewHolder -> holder.bind(item = items[position] as AppThemeObject)
//            is FlagQuizSingleViewHolder -> holder.bind(item = items[position] as FlagQuizSingleObject)
            is TriviaAnswerSingleViewHolder -> holder.bind(item = items[position] as TriviaAnswerSingleObject)
        }
    }

    /**
     *
     */
    override fun getType(position: Int): Int = items[position]?.getViewType() ?: (-1)
}