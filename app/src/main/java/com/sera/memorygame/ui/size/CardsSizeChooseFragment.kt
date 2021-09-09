package com.sera.memorygame.ui.size

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.sera.memorygame.R
import com.sera.memorygame.database.model.SizeViewObject
import com.sera.memorygame.databinding.CardsSizeChooseFragmentBinding
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.ui.adapter.BaseRecyclerViewAdapter
import com.sera.memorygame.ui.adapter.CommonAdapter
import com.sera.memorygame.utils.Constants
import com.sera.memorygame.viewModel.CardsSizeChooseViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CardsSizeChooseFragment : BaseFragment() {

    private lateinit var mBinder: CardsSizeChooseFragmentBinding

    private val args: CardsSizeChooseFragmentArgs by navArgs()

    /**
     *
     */
    @Inject
    lateinit var viewModel: CardsSizeChooseViewModel


    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.cards_size_choose_fragment, container, false)
        return mBinder.root
    }

    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
        generateAdapter()
        Handler(Looper.myLooper() ?: Looper.getMainLooper()).postDelayed({
            setList()
        }, Constants.MIN_DELAY_TIME)
    }

    /**
     *
     */
    private fun generateAdapter() {
        with(mBinder.recycler) {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = CommonAdapter(handlers = this@CardsSizeChooseFragment)
            itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
            GravitySnapHelper(Gravity.TOP).attachToRecyclerView(this)
        }
    }

    /**
     *
     */
    private fun setList() {
        (mBinder.recycler.adapter as BaseRecyclerViewAdapter).setList(list = viewModel.getList(jsonReference = args.jsonReference))
    }

    /**
     *action_cardsSizeChooseFragment_to_memoryFragment
     */
    override fun onHandleClickedWithPosition(view: View, position: Int) {
        when (view.id) {
            R.id.mainView -> {
                ((mBinder.recycler.adapter as BaseRecyclerViewAdapter).getItemByPosition(position = position) as? SizeViewObject?)?.let {
                    navigate(CardsSizeChooseFragmentDirections.actionCardsSizeChooseFragmentToMemoryFragment(jsonRef = args.jsonReference, sizeObj = it))
                }
            }
        }
    }
}