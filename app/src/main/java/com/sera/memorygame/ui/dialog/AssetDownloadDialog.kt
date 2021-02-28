package com.sera.memorygame.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sera.memorygame.R
import com.sera.memorygame.databinding.AssetDialogLayoutBinding

class AssetDownloadDialog : BaseDialogFragment() {

    private lateinit var mBinder: AssetDialogLayoutBinding

    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.asset_dialog_layout, container, false)
        return mBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
    }
}