package com.sera.memorygame.ui.start

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.sera.memorygame.R
import com.sera.memorygame.databinding.StartFragmentBinding
import com.sera.memorygame.ui.BaseActivity
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.ui.MainActivity
import com.sera.memorygame.ui.flag_quiz.FlagQuizContainerFragment
import com.sera.memorygame.ui.theme.GameThemeFragment
import com.sera.memorygame.ui.trivia.TriviaContainerFragment
import com.sera.memorygame.viewModel.StartViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
class StartFragment : BaseFragment() {
    private lateinit var mBinder: StartFragmentBinding

    /**
     *
     */
    companion object {
        fun newInstance() = StartFragment()
    }

    @Inject
    lateinit var viewModel: StartViewModel

    /**
     *
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as? MainActivity?)?.mainComponent?.inject(fragment = this)
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
                (requireActivity() as MainActivity).replaceFragment(fragment = GameThemeFragment.newInstance())
            }
            R.id.quizBtn -> {
                (requireActivity() as? BaseActivity?)?.replaceFragment(fragment = FlagQuizContainerFragment.newInstance())
            }
            R.id.triviaBtn -> {
                (requireActivity() as? BaseActivity?)?.replaceFragment(fragment = TriviaContainerFragment.newInstance())
            }
        }
    }

}