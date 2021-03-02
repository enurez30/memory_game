package com.sera.memorygame.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.sera.memorygame.R
import com.sera.memorygame.databinding.StartFragmentBinding
import com.sera.memorygame.factory.StartFragmentFactory
import com.sera.memorygame.ui.BaseActivity
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.ui.MainActivity
import com.sera.memorygame.ui.theme.GameThemeFragment
import com.sera.memorygame.utils.AnimationHelper
import com.sera.memorygame.utils.NetworkStatus
import com.sera.memorygame.viewModel.StartViewModel

class StartFragment : BaseFragment() {
    private lateinit var mBinder: StartFragmentBinding
    private var snackbar: Snackbar? = null

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
        prepareView()
        addObserver()
//        (requireActivity() as BaseActivity).getAssetWorkerExecutingStatus.observe(viewLifecycleOwner, {
//            println("DOWNLOAD: status - $it")
//        })
//        Handler(Looper.myLooper() ?: Looper.getMainLooper()).postDelayed({
//            releaseView()
//        }, 500)

    }

    /**
     *
     */
    private fun addObserver() {
        with(requireActivity() as BaseActivity) {
            this.getAssetWorkerExecutingStatus.observe(viewLifecycleOwner, {
                when (it) {
                    NetworkStatus.START.status -> {
                        snackbar?.dismiss()
                        this.showSnackBar("Check for updates", duration = Snackbar.LENGTH_INDEFINITE, view = mBinder.root) { sb ->
                            snackbar = (sb as Snackbar)
                        }
                    }
                    NetworkStatus.DOWNLOAD.status -> {
                        snackbar?.dismiss()
                        this.showSnackBar("Downloading Assets", duration = Snackbar.LENGTH_INDEFINITE, view = mBinder.root) { sb ->
                            snackbar = (sb as Snackbar)
                        }
                    }
                    NetworkStatus.ERROR.status -> {
                        snackbar?.dismiss()
                        this.showSnackBar("Error", duration = Snackbar.LENGTH_INDEFINITE, view = mBinder.root) { sb ->
                            snackbar = (sb as Snackbar)
                        }
                    }
                    NetworkStatus.NO_INTERNET_CONNECTION.status -> {
                        snackbar?.dismiss()
                        this.showSnackBar("No internet connection, try again later", duration = Snackbar.LENGTH_INDEFINITE, view = mBinder.root) { sb ->
                            snackbar = (sb as Snackbar)
                        }
                    }
                    NetworkStatus.FINISH.status -> {
                        snackbar?.dismiss()
                        releaseView()
                    }
                }
            })
        }

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
//                AssetDownloadDialog.newInstance().show(requireActivity().supportFragmentManager, "")
            }
        }
    }


}