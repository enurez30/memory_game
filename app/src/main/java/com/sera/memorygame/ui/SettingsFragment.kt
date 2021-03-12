package com.sera.memorygame.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sera.memorygame.R
import com.sera.memorygame.database.repository.UserRepository
import com.sera.memorygame.databinding.SettingsFragmentBinding
import com.sera.memorygame.extentions.themeColor
import com.sera.memorygame.factory.SettingsFactory
import com.sera.memorygame.factory.UserViewModelFactory
import com.sera.memorygame.ui.dialog.AppThemeDialog
import com.sera.memorygame.ui.dialog.UserDialog
import com.sera.memorygame.utils.Prefs
import com.sera.memorygame.viewModel.SettingsViewModel
import com.sera.memorygame.viewModel.UserViewModel
import kotlinx.coroutines.flow.collect


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
        SettingsFactory(context = requireContext(), repo = UserRepository(context = requireContext()))
    }

    /**
     *
     */
    private val userViewModel by viewModels<UserViewModel> {
        UserViewModelFactory(context = requireContext(), repo = UserRepository(context = requireContext()))
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
        generateThemeView()
        lifecycleScope.launchWhenCreated {
            addObservers()
        }
    }

    /**
     *
     */
    private suspend fun addObservers() {
        userViewModel.getUserInSession().collect {
            generateUserView()
        }
    }

    /**
     *
     */
    private fun generateUserView() {
        mBinder.uLayout.userName.text = userViewModel.getName()
    }

    /**
     *
     */
    private fun generateThemeView() {
        val primaryColor = requireContext().themeColor(attrRes = R.attr.colorPrimary)
        val secondaryColor = requireContext().themeColor(attrRes = R.attr.colorSecondaryVariant)
        mBinder.tLayout.circleView.setColors(mainColor = primaryColor, secondaryColor = secondaryColor)
        mBinder.tLayout.themeName.text = Prefs.getThemeName()
    }

    /**
     *
     */
    override fun onHandlerClicked(view: View) {
        when (view.id) {
            R.id.themeChangeTV -> {
                AppThemeDialog.newInstance().show(requireActivity().supportFragmentManager, AppThemeDialog::class.java.simpleName)
            }
            R.id.updateIV -> {
                UserDialog.newInstance().show(requireActivity().supportFragmentManager, UserDialog::class.java.simpleName)
            }
        }
    }
}