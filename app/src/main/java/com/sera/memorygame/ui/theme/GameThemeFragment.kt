package com.sera.memorygame.ui.theme

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.sera.memorygame.R
import com.sera.memorygame.database.model.GameThemeObject
import com.sera.memorygame.databinding.GameThemeFragmentBinding
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.ui.adapter.BaseRecyclerViewAdapter
import com.sera.memorygame.ui.adapter.CommonAdapter
import com.sera.memorygame.utils.Constants
import com.sera.memorygame.viewModel.GameThemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class GameThemeFragment : BaseFragment() {

    private lateinit var mBinder: GameThemeFragmentBinding

    @Inject
    lateinit var viewModel: GameThemeViewModel

    /**
     *
     */
    companion object {
        fun newInstance() = GameThemeFragment()
    }


    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.game_theme_fragment, container, false)
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
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CommonAdapter(handlers = this@GameThemeFragment)
            itemAnimator = SlideInLeftAnimator(OvershootInterpolator(1f))
            GravitySnapHelper(Gravity.TOP).attachToRecyclerView(this)
        }
    }

    /**
     *
     */
    private fun setList() {
        (mBinder.recycler.adapter as BaseRecyclerViewAdapter).setList(list = viewModel.getList())
    }

    /**
     *
     */
    override fun onHandleClickedWithPosition(view: View, position: Int) {
        when (view.id) {
            R.id.mainView -> {
                ((mBinder.recycler.adapter as BaseRecyclerViewAdapter).getItemByPosition(position = position) as? GameThemeObject?)?.let {
                    navigate(GameThemeFragmentDirections.actionGameThemeFragmentToCardsSizeChooseFragment(jsonReference = it.jsonReference))
                }
            }
        }
    }
}