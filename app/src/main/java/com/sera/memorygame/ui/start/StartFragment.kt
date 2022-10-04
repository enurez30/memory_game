package com.sera.memorygame.ui.start

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sera.memorygame.R
import com.sera.memorygame.database.entity.HistoryEntity
import com.sera.memorygame.databinding.StartFragmentBinding
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.utils.Constants
import com.sera.memorygame.viewModel.StartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_layout.view.*
import kotlinx.coroutines.*
import javax.inject.Inject


@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class StartFragment : BaseFragment() {
    private lateinit var mBinder: StartFragmentBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    @Inject
    lateinit var viewModel: StartViewModel


    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.start_fragment,
            container,
            false
        )
        return mBinder.root
    }

    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
        buildBottomView()
        lifecycleScope.launchWhenCreated {
            addObserver()
        }
    }

    /**
     *
     */
    private suspend fun addObserver() {
        viewModel.getData().collect { indicator ->
            when (indicator.type) {
                Constants.HISTORY_QUIZ_GAME_TYPE -> {
                    mBinder.quizIndicator.isVisible = indicator.isVisible
                }
                Constants.HISTORY_TRIVIA_GAME_TYPE -> {
                    mBinder.triviaIndicator.isVisible = indicator.isVisible
                }
            }
        }
    }

    /**
     *
     */
    private fun buildBottomView() {
        bottomSheetBehavior = BottomSheetBehavior.from(mBinder.mainView.bottom_view_root)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
                if (p1 in 0F..1F) {
                    mBinder.backOverlay.visibility = View.VISIBLE
                    mBinder.backOverlay.alpha = p1
                }

            }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(p0: View, p1: Int) {
                when (p1) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        mBinder.backOverlay.isClickable = false
                        mBinder.backOverlay.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        mBinder.backOverlay.isClickable = true
                        mBinder.backOverlay.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    /**
     *
     */
    private fun toggleBottomSheet() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    /**
     *
     */
    override fun delegateHandlerClick(view: View) {
        when (view.id) {
            R.id.memoryBtn -> {
                navigate(StartFragmentDirections.actionStartFragmentToGameThemeFragment())
            }
            R.id.quizBtn -> {
                viewModel.hQuizState.value?.let {
                    mBinder.mainView.bottom_view_root.tag = it
                    toggleBottomSheet()
                } ?: kotlin.run {
                    navigate(StartFragmentDirections.actionStartFragmentToFlagQuizContainerFragment())
                }
            }
            R.id.triviaBtn -> {
                viewModel.hTriviaState.value?.let {
                    mBinder.mainView.bottom_view_root.tag = it
                    toggleBottomSheet()
                } ?: kotlin.run {
                    navigate(StartFragmentDirections.actionStartFragmentToTriviaContainerFragment())
                }
            }
            R.id.backOverlay -> {
                toggleBottomSheet()
            }
            R.id.continueTV -> {
                lifecycleScope.launch(Dispatchers.Main) {
                    toggleBottomSheet()
                    delay(350)
                    mBinder.mainView.bottom_view_root.tag?.let {
                        (it as? HistoryEntity)?.let { he ->
                            when (he.type) {
                                Constants.HISTORY_QUIZ_GAME_TYPE -> {
                                    navigate(StartFragmentDirections.actionStartFragmentToFlagQuizContainerFragment())
                                }
                                Constants.HISTORY_TRIVIA_GAME_TYPE -> {
                                    navigate(StartFragmentDirections.actionStartFragmentToTriviaContainerFragment())
                                }
                                else -> {
                                    println("some error")
                                }
                            }
                        }
                    }
                }
            }
            R.id.resetTV -> {
                lifecycleScope.launch(Dispatchers.Main) {
                    toggleBottomSheet()
                    delay(350)
                    mBinder.mainView.bottom_view_root.tag?.let {
                        (it as? HistoryEntity)?.let { he ->
                            lifecycleScope.launch {
                                viewModel.deleteHistory(obj = he)
                            }
                        }
                    }
                }
            }
        }
    }
}