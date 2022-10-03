package com.sera.memorygame.ui.trivia

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.sera.memorygame.R
import com.sera.memorygame.database.entity.TriviaEntity
import com.sera.memorygame.database.model.ScoreObject
import com.sera.memorygame.databinding.FragmentTriviaContainerBinding
import com.sera.memorygame.event.MessageEvent
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.utils.AnimationHelper
import com.sera.memorygame.utils.Constants
import com.sera.memorygame.viewModel.TriviaUiState
import com.sera.memorygame.viewModel.TriviaViewModel
import com.transitionseverywhere.ChangeText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TriviaContainerFragment : BaseFragment() {
    private lateinit var mBinder: FragmentTriviaContainerBinding
    private var firstRun: Boolean = true
    private var isAnimated: Boolean = false
    private var timer: CountDownTimer? = null

    @Inject
    lateinit var viewModel: TriviaViewModel

    /**
     *
     */
    companion object {
        fun newInstance() = TriviaContainerFragment()
    }

    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.fragment_trivia_container, container, false)
        return mBinder.root
    }

    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
        if (firstRun) {
            showChooseFragment()
        }
        animateScore()
        lifecycleScope.launch {
            addObservers()
        }
    }

    /**
     *
     */
    private fun showChooseFragment() {
        replaceFragment(fragment = TriviaChooseFragment.newInstance())
    }

    /**
     *
     */
    private suspend fun addObservers() {
        viewModel.getData().collect { state ->
            when (state) {
                is TriviaUiState.History -> {
                    state.entity?.let { he ->
                        updateScoreView(score = he.score)
                    }
                }
                is TriviaUiState.Trivia -> {
                    println("TRIVIA: list size = ${state.list}")
                    mBinder.camomileSpinner.visibility = View.GONE

                    if ((state.list as? List<*>)?.isEmpty() == true) {
                        // game over
                        if (!firstRun) {
                            // add some animation
                            if (!isAnimated) {
                                isAnimated = true
                                animateView(konfettiView = mBinder.viewKonfetti)
                                delay(Constants.DEFAULT_AWAIT_TIME)
                                isAnimated = false
                                startOver()
                            }
                        }
                    } else {
                        animateScore(show = true)
                        firstRun = false
                        viewModel.updateHistoryTotal(total = (state.list as? List<*>)?.size ?: 0)
                        setNextItem()
                    }
                }
            }
        }
    }


    /**
     *
     */
    private fun updateScoreView(score: ScoreObject) {
        with(mBinder.scoreInclude) {
            val answered = score.correct + score.wrong
            totalTV.text = resources.getString(R.string.quiz_total, answered, score.total)
            correctTV.text = score.correct.toString()
            incorrectTV.text = score.wrong.toString()

            TransitionManager.beginDelayedTransition(quizScoreMainView, ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_KEEP))
        }
    }


    /**
     *
     */
    private fun setNextItem() {
        val item = viewModel.getTriviaObject()
        updateCategoryView(item = item)
        replaceFragment(fragment = TriviaFragment.newInstance(entity = item))
    }

    /**
     *
     */
    private fun updateCategoryView(item: TriviaEntity) {
        with(mBinder.triviaInclude) {
            categoryTV.text = item.category
        }
        startTimer()
    }

    /**
     *
     */
    private fun startTimer() {
        if (timer == null) {
            timer = object : CountDownTimer(20000, 1000) {
                override fun onTick(p0: Long) {
                    if (isAdded) {
                        setTimerValue(value = (p0 / 1000).toInt())
                    }
                }

                override fun onFinish() {
                    notifyFinishTimer()
                    cancel()
                    timer = null
                }

            }
        }
        timer?.start()
    }

    /**
     *
     */
    private fun setTimerValue(value: Int) {
        val color = when (value) {
            in 8..12 -> {
                AnimationHelper.scaleViewAnimation(target = mBinder.triviaInclude.timerTV, scaleTo = 1.25F)
                ContextCompat.getColor(requireContext(), R.color.yellow)
            }
            in 0..7 -> {
                AnimationHelper.scaleViewAnimation(target = mBinder.triviaInclude.timerTV)
                ContextCompat.getColor(requireContext(), R.color.red_700Dark)
            }
            else -> {
                ContextCompat.getColor(requireContext(), R.color.grey_700)
            }
        }

        mBinder.triviaInclude.timerTV.setTextColor(color)
        mBinder.triviaInclude.timerTV.text = value.toString()

    }

    /**
     *
     */
    private fun notifyFinishTimer() {
        if (isAdded) {
            (childFragmentManager.findFragmentByTag(TriviaFragment::class.java.simpleName) as? TriviaFragment)?.notifyOnTimerFinish()
        }
    }

    /**
     *
     */
    private fun animateScore(show: Boolean = false) {
        with(mBinder) {
            if (show) {
                if (scoreInclude.quizScoreMainView.visibility == View.GONE) {
                    scoreInclude.quizScoreMainView.visibility = View.VISIBLE
                    triviaInclude.triviaCategoryMain.visibility = View.VISIBLE
                    TransitionManager.beginDelayedTransition(mainContainer, AutoTransition())
                }
            } else {
                scoreInclude.quizScoreMainView.visibility = View.GONE
                triviaInclude.triviaCategoryMain.visibility = View.GONE
                TransitionManager.beginDelayedTransition(mainContainer, AutoTransition())
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
    private fun goNext(message: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getTriviaObjects.value?.find {
                it.id == message
            }?.let { trivia ->
                viewModel.deleteObject(obj = trivia)
            } ?: kotlin.run {
                this.launch(Dispatchers.Main) {
                    animateView(konfettiView = mBinder.viewKonfetti)
                }
            }
        }
    }


    /**
     *
     */
    private fun startOver() {
        viewModel.resetHistory()
        animateScore(show = false)
        showChooseFragment()
    }

    /**
     *
     */
    private fun updateScore(message: String, id: String) {
        when (message) {
            "correct" -> {
                mBinder.scoreInclude.correctTV.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.bounce))
            }
            "wrong" -> {
                AnimationHelper.shakeView(view = mBinder.scoreInclude.incorrectTV, 100, 5)
            }
        }
        viewModel.updateTriviaScore(value = message, id = id)
        timer?.cancel()
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
        if (event.reciever == TriviaContainerFragment::class.java.simpleName) {
            when (event.key) {
                "nextQuiz" -> {
                    goNext(message = event.message)
                }
                else -> updateScore(message = event.message, id = event.key)
            }
        }
    }
}