package com.sera.memorygame.ui.memory

import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.sera.memorygame.R
import com.sera.memorygame.custom.MemoryCardView
import com.sera.memorygame.database.model.IObject
import com.sera.memorygame.databinding.FragmentMemoryBinding
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.ui.adapter.BaseRecyclerViewAdapter
import com.sera.memorygame.ui.adapter.CommonAdapter
import com.sera.memorygame.utils.Constants
import com.sera.memorygame.viewModel.MemoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class MemoryFragment : BaseFragment(), Handlers {

    private lateinit var mBinder: FragmentMemoryBinding

    private val args: MemoryFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: MemoryViewModel


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
        mBinder.handlers = this
        setValues()
        addObservers()
        generateAdapter()
        mBinder.container.post {
            viewModel.generateMemoryViewObject(
                context = requireContext(),
                memoryObjeList = viewModel.generateMemoryObjects(
                    containerW = mBinder.container.width,
                    containerH = mBinder.container.height
                ),
                handlers = this@MemoryFragment
            ).let { list ->
                (mBinder.recycler.adapter as BaseRecyclerViewAdapter).setList(list = ArrayList<IObject>().run {
                    this.addAll(list)
                    this
                })
                animateLayout()
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
            adapter = CommonAdapter()
        }
    }

    /**
     *
     */
    private fun addObservers() {
        viewModel.updatePair.observe(viewLifecycleOwner, {
            if (it.first != null && it.second != null) {
                mBinder.overlay.visibility = View.VISIBLE
                if (it.first.tag == it.second.tag) {
                    val prev = viewModel.getControlValue.value ?: 0
                    viewModel.getControlValue.value = prev + 2

                    animateShowKonfetti(konfettiView = mBinder.viewKonfetti)
                    viewModel.updatePair.value = MemoryViewModel.MemoryPair(null, null)
                } else {
                    lifecycleScope.launch {
                        delay(1000L)
                        it.first.reset()
                        it.second.reset()
                        viewModel.updatePair.value = MemoryViewModel.MemoryPair(null, null)
                    }
                }
            } else {
                mBinder.overlay.visibility = View.GONE
            }
        })
        viewModel.getControlValue.observe(viewLifecycleOwner, {
            it?.let {
                if (it == viewModel.memoryListValue.value) {
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
        })
    }

    /**
     *
     */
    override fun onHandlerClicked(view: View) {
        // memory card was clicked
        (view as? MemoryCardView)?.let { cardView ->
            viewModel.updatePair.value?.let {
                var pair: MemoryViewModel.MemoryPair = it
                when {
                    it.first == null -> {
                        pair = MemoryViewModel.MemoryPair(cardView, null)
                    }
                    it.second == null -> {
                        pair = MemoryViewModel.MemoryPair(it.first, cardView)
                    }
                }
                viewModel.updatePair.value = pair
            }
        }
    }

    /**
     *
     */
    override fun onHandleClickedWithPosition(view: View, position: Int) {

    }

    /**
     *
     */
    override fun onLongClicked(view: View, position: Int?): Boolean = false
}
