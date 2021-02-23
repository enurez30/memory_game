package com.sera.memorygame.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.sera.memorygame.R
import com.sera.memorygame.databinding.StartFragmentBinding
import com.sera.memorygame.factory.StartFragmentFactory
import com.sera.memorygame.utils.AnimationHelper
import com.sera.memorygame.viewModel.StartViewModel

class StartFragment : BaseFragment() {
    private lateinit var mBinder: StartFragmentBinding

    /**
     *
     */
    companion object {
        fun newInstance() = StartFragment()
    }

    /**
     *
     */
    private val viewModel by viewModels<StartViewModel> {
        StartFragmentFactory(context = requireContext())
    }

    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.start_fragment, container, false)
        return mBinder.root
    }

    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
        prepareView()
        Handler(Looper.myLooper() ?: Looper.getMainLooper()).postDelayed({
            releaseView()
        }, 500)

    }

    /**
     *
     */
    private fun prepareView() {
        mBinder.playBtn.animate().translationY(1000F).setDuration(0L).start()
    }

    /**
     *
     */
    private fun releaseView() {
        AnimationHelper.animateSpringY(targetView = mBinder.playBtn, finalPosition = 0F)
    }

    /**
     *
     */
    override fun delegateHandlerClick(view: View) {
        when (view.id) {
            R.id.playBtn -> {
                (requireActivity() as MainActivity).replaceFragment(fragment = GameThemeFragment.newInstance())
            }
        }
    }
}