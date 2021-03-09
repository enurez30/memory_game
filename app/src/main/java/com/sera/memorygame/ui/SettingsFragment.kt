package com.sera.memorygame.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.sera.memorygame.R
import com.sera.memorygame.databinding.SettingsFragmentBinding
import com.sera.memorygame.factory.SettingsFactory
import com.sera.memorygame.ui.dialog.AppThemeDialog

class SettingsFragment : BaseFragment() {
    private lateinit var mBinder: SettingsFragmentBinding

    /**
     *
     */
    companion object {
        fun newInstance() = SettingsFragment()
    }

    /**
     *
     */
    private val viewModel by viewModels<SettingsViewModel> {
        SettingsFactory(context = requireContext())
    }

    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.settings_fragment, container, false)
        return mBinder.root
    }

    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
    }

    /**
     *
     */
    override fun onHandlerClicked(view: View) {
        when (view.id) {
            R.id.themeChangeTV -> {
                AppThemeDialog.newInstance().show(requireActivity().supportFragmentManager, AppThemeDialog::class.java.simpleName)
            }
        }
    }
}