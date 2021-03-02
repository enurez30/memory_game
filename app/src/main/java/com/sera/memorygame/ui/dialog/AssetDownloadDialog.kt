package com.sera.memorygame.ui.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.sera.memorygame.event.MessageEvent
import com.sera.memorygame.R
import com.sera.memorygame.databinding.AssetDialogLayoutBinding
import com.sera.memorygame.service.AssetWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AssetDownloadDialog : BaseDialogFragment() {

    private lateinit var mBinder: AssetDialogLayoutBinding
    private var counter = 0

    /**
     *
     */
    companion object {
        fun newInstance() = AssetDownloadDialog()
    }

    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.asset_dialog_layout, container, false)
        return mBinder.root
    }

    /**
     *
     */
    override fun delegateEventResult(event: MessageEvent) {
        if (event.reciever == this::class.java.simpleName) {
            println("DOWNLOAD: received event= ${event.key}")
            when (event.key) {
                "error" -> {

                }
                "finish" -> {
                    mBinder.contentTV.text = event.message
                    mBinder.progressIndicator.trackThickness = 50
                    mBinder.progressIndicator.max = 127
                    mBinder.progressIndicator.setProgressCompat(0, true)
                    mBinder.progressIndicator.trackColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
                    mBinder.progressIndicator.setIndicatorColor(ContextCompat.getColor(requireContext(), R.color.colorPrimarySecondary))
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            lifecycleScope.launch(Dispatchers.Main) {
                                counter++
                                mBinder.progressIndicator.progress = counter
                                if (counter == 127) {
                                    Handler(Looper.myLooper() ?: Looper.getMainLooper()).postDelayed({
                                        dismiss()
                                    }, 1000)
                                }
                            }
                        }
                    }, 0, 20)
                }
            }
        }
    }

    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
        setUpView()
        Handler(Looper.myLooper() ?: Looper.getMainLooper()).postDelayed({
            WorkManager.getInstance(requireContext()).enqueue(OneTimeWorkRequestBuilder<AssetWorker>().build())
        }, 500)
    }

    /**
     *
     */
    private fun setUpView() {
        mBinder.titleTV.text = requireContext().getString(R.string.notice)
        mBinder.contentTV.text = "Checking for an update"
    }

}