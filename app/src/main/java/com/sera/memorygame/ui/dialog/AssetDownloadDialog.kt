package com.sera.memorygame.ui.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.sera.memorygame.MessageEvent
import com.sera.memorygame.R
import com.sera.memorygame.databinding.AssetDialogLayoutBinding
import com.sera.memorygame.service.AssetWorker

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
            when (event.key) {
                "status" -> {
                    mBinder.contentTV.text = event.message
                }
                "max_length" -> {
                    mBinder.progressIndicator.max = (event.message).toInt()
                    requireActivity().runOnUiThread {
                        mBinder.progressIndicator.visibility = View.VISIBLE
                        mBinder.bottomTv.visibility = View.VISIBLE
                        updateText(from = "0", to = mBinder.progressIndicator.max.toString())
                    }
                }
                "progress_update" -> {
                    counter++
                    mBinder.progressIndicator.progress = counter
                    requireActivity().runOnUiThread {
                        updateText(from = mBinder.progressIndicator.progress.toString(), to = mBinder.progressIndicator.max.toString())
                    }
                }
                "error" -> {

                }
                "finish" -> {
                    mBinder.contentTV.text = event.message
                    Handler(Looper.myLooper() ?: Looper.getMainLooper()).postDelayed({
                        dismiss()
                    }, 1000)
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
//        mBinder.progressIndicator.visibility = View.INVISIBLE
//        mBinder.bottomTv.visibility = View.INVISIBLE
    }

    /**
     *
     */
    private fun updateText(from: String, to: String) {
        println("DOWNLOAD: Downloaded $from of $to")
        mBinder.bottomTv.text = "Downloaded $from of $to"
    }


}