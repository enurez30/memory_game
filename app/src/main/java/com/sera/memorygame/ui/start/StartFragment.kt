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
    }


    /**
     *
     */
    override fun delegateHandlerClick(view: View) {
        when (view.id) {
            R.id.memoryBtn -> {
//                UserRepository(context = requireContext()).deleteAllUsers()
                (requireActivity() as MainActivity).replaceFragment(fragment = GameThemeFragment.newInstance())

            }
            R.id.quizBtn -> {
//                UserRepository(context = requireContext()).createUser().apply {
//                    UserRepository(context = requireContext()).persistUser(user = this)
//                }
                (requireActivity() as? BaseActivity)?.showSnackBar("in development", view = mBinder.mainView)
            }
        }
    }


}