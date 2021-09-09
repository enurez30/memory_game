package com.sera.memorygame.ui.flag_quiz

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionManager
import com.sera.memorygame.R
import com.sera.memorygame.database.entity.CountryEntity
import com.sera.memorygame.databinding.FragmentFlagQuizContainerBinding
import com.sera.memorygame.event.MessageEvent
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.utils.AnimationHelper
import com.sera.memorygame.utils.Constants
import com.sera.memorygame.viewModel.CountryViewModel
import com.transitionseverywhere.ChangeText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FlagQuizContainerFragment : BaseFragment() {
    private lateinit var mBinder: FragmentFlagQuizContainerBinding
    private var isAnimated: Boolean = false

    @Inject
    lateinit var countryViewModel: CountryViewModel

    /**
     *
     */
    companion object {
        fun newInstance() = FlagQuizContainerFragment()
    }


    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.fragment_flag_quiz_container, container, false)
        return mBinder.root
    }

    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
        lifecycleScope.launch {
            delay(100)
            addObserver()
        }
    }

    /**
     *
     */
    private suspend fun addObserver() {
        countryViewModel.getRemainCountriesLive.observe(viewLifecycleOwner, {
            if (it?.isNotEmpty() == true) {
                setNextItem()
            } else {
                // startOver
                if (!isAnimated) {
                    isAnimated = !isAnimated
                    animateView(konfettiView = mBinder.viewKonfetti)
                    Handler(Looper.getMainLooper()).postDelayed({
                        lifecycleScope.launch {
                            requireActivity().supportFragmentManager.popBackStack()
                        }
                    }, Constants.DEFAULT_AWAIT_TIME)
                }
            }
        })

        val flowA = flow { emitAll(countryViewModel.getCorrectCountries()) }
        val flowB = flow { emitAll(countryViewModel.getWrongCountries()) }
        merge(flowA, flowB).collect {
            updateScoreValues()
        }
    }


    /**
     *
     */
    private fun setNextItem() {
        replaceFragment(fragment = FlagQuizFragment.newInstance(entity = countryViewModel.getQuizObject()))
    }

    /**
     *
     */
    private fun getResult(key: String, message: String) {
        val country = (countryViewModel.getRemainCountriesLive.value as ArrayList<CountryEntity>).find { it.id == message }
        when (key) {
            "correct" -> {
                country?.let {
                    val oldValue = countryViewModel.getCorrectCountries().value
                    val newValue = oldValue + 1
                    countryViewModel.setCorrectValue(newValue = newValue)
                    mBinder.scoreInclude.correctTV.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.bounce))
                }
            }
            "wrong" -> {
                country?.let {
                    val oldValue = countryViewModel.getWrongCountries().value
                    val newValue = oldValue + 1
                    countryViewModel.setWrongCountries(newValue = newValue)
                    AnimationHelper.shakeView(view = mBinder.scoreInclude.incorrectTV, 100, 5)
                }
            }
        }
        lifecycleScope.launch {
            countryViewModel.updateHistoryObject(countryId = message)
        }
    }

    /**
     *
     */
    private fun goNext(message: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            (countryViewModel.getRemainCountriesLive.value as ArrayList<CountryEntity>).removeIf { it.id == message }
            countryViewModel.getRemainCountriesLive.value = countryViewModel.getRemainCountriesLive.value
        }
    }

    /**
     *
     */
    private suspend fun updateScoreValues() = withContext(Dispatchers.Main) {
        with(countryViewModel) {
            val (totalCountries, correct, wrong, total) = getScoreValues()

            with(mBinder.scoreInclude) {
                totalTV.text = resources.getString(R.string.quiz_total, total, totalCountries)
                correctTV.text = resources.getString(R.string.quiz_correct, correct)
                incorrectTV.text = resources.getString(R.string.quiz_wrong, wrong)

                TransitionManager.beginDelayedTransition(quizScoreMainView, ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_KEEP))

            }
        }

    }


    /**
     *
     */
    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, fragment::class.java.simpleName)
            .addToBackStack("fragment")
            .commit()
    }

    /**
     *
     */
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    /**
     *
     */
    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    /**
     *
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onEvent(event: MessageEvent) {
        if (event.reciever == FlagQuizContainerFragment::class.java.simpleName) {
            when (event.key) {
                "next" -> {
                    goNext(message = event.message)
                }
                else -> getResult(key = event.key, message = event.message)
            }
        }
    }

}