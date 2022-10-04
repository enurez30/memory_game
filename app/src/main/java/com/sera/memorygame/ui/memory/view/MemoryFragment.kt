package com.sera.memorygame.ui.memory.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.sera.memorygame.R
import com.sera.memorygame.database.model.MemoryViewObject
import com.sera.memorygame.databinding.FragmentMemoryBinding
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.ui.memory.MemoryCardAnimationState
import com.sera.memorygame.ui.memory.data.MemoryMatch
import com.sera.memorygame.ui.memory.viewmodel.MemoryViewModel
import com.sera.memorygame.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class MemoryFragment : BaseFragment() {

    private lateinit var mBinder: FragmentMemoryBinding
    private val args: MemoryFragmentArgs by navArgs()
    private val viewModel: MemoryViewModel by viewModels()


    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.fragment_memory,
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
        setValues()
        generateAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeUi()
                observeAnimationState()
            }
        }

        mBinder.container.post {
            viewModel.updateConteinerDimentions(width = mBinder.container.width, height = mBinder.container.height)
        }

    }

    private fun CoroutineScope.observeUi() {
        viewModel.uiState.onEach { state ->
            setList(list = state.cards)
            observeMatchPair(matchPair = state.matchPair)
            observeEndGame(total = state.sizeObject?.size ?: -1, matched = state.matchedPairsValue)
        }.launchIn(this)
    }

    private fun CoroutineScope.observeAnimationState() {
        viewModel.memoryCardAnimationState.onEach { animationState ->
            when (animationState) {
                MemoryCardAnimationState.ANIMATION_IN_PROGRESS -> mBinder.overlay.visibility = View.VISIBLE
                MemoryCardAnimationState.ANIMATION_ENDED -> mBinder.overlay.visibility = View.GONE
            }
        }.launchIn(this)
    }

    private fun setList(list: List<MemoryViewObject>) {
        (mBinder.recycler.adapter as MemoryAdapter).submitList(list)
    }

    private fun observeMatchPair(matchPair: Pair<MemoryMatch?, MemoryMatch?>?) {
        matchPair?.let { pair ->

            if (pair.first?.isHide == true) {
                pair.first?.memoryCardView?.reset()
            }
            if (pair.first?.isShow == true) {
                pair.first?.memoryCardView?.showCard()
            }
            if (pair.second?.isHide == true) {
                pair.second?.memoryCardView?.reset()
            }
            if (pair.second?.isShow == true) {
                pair.second?.memoryCardView?.showCard()
            }
        }
    }

    private fun observeEndGame(total: Int, matched: Int) {
        if (total != -1 && (total / 2) == matched) {
            // game over
            animateView(konfettiView = mBinder.viewKonfetti)
            lifecycleScope.launch {
                delay(Constants.DEFAULT_AWAIT_TIME)
                this.launch(Dispatchers.Main) {
                    requireActivity().onBackPressed()
                }
            }
        }
    }

    /**
     *
     */
    private fun setValues() {
        viewModel.setValues(
            sizeViewObject = args.sizeObj,
            jsonRef = args.jsonRef
        )
    }

    /**
     *
     */
    private fun animateLayout() {
        mBinder.recycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
    }

    /**
     *
     */
    private fun generateAdapter() {
        val sizeViewObject = args.sizeObj
        with(mBinder.recycler) {
            layoutManager = GridLayoutManager(requireContext(), sizeViewObject.xAxis)
            itemAnimator = SlideInLeftAnimator(OvershootInterpolator(1f))
            adapter = MemoryAdapter()
        }
    }

}
