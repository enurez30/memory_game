package com.sera.memorygame.ui.flag_quiz.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.sera.memorygame.R
import com.sera.memorygame.database.model.FlagQuizMainObject
import com.sera.memorygame.databinding.FlagQuizFragmentBinding
import com.sera.memorygame.extentions.toAbbreviation
import com.sera.memorygame.providers.ResourcesProvider
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.ui.adapter.FlagsAdapter
import com.sera.memorygame.ui.flag_quiz.setImageDrawableWithAnimation
import com.sera.memorygame.ui.flag_quiz.toFlagDimentions
import com.sera.memorygame.ui.flag_quiz.viewmodel.CountryViewModel
import com.sera.memorygame.utils.AnimationHelper
import com.sera.memorygame.utils.Constants
import com.sera.memorygame.utils.Prefs
import com.transitionseverywhere.ChangeText
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.internal.toImmutableList
import javax.inject.Inject


@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FlagQuizFragment : BaseFragment() {
    private lateinit var mBinder: FlagQuizFragmentBinding
    private var isAnimated: Boolean = false
    private val countryViewModel: CountryViewModel by viewModels()

    @Inject
    lateinit var provider: ResourcesProvider

    /**
     *
     */
    private val keyLiveData by lazy {
        MutableLiveData("")
    }

    /**
     *
     */
    companion object {
        fun newInstance() = FlagQuizFragment()
    }


    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.flag_quiz_fragment, container, false)
        return mBinder.root
    }

    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
        generateAdapter()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                delay(100)
//                countryObserver()
//                observeFlag()
//                addObserver()
                observeUiState()
            }
        }

    }

    private fun CoroutineScope.observeUiState() {
        countryViewModel.uiState.filterNotNull().onEach {
            if (it.flagQuizObject != null) {
                setFlag(item = it.flagQuizObject)
                delay(100)
                setList(item = it.flagQuizObject)
                updateScoreValues(cAmount = it.totalCountries, correct = it.correctCountries, wrong = it.wrongCountries)
            }
            addObserver(isGoNext = it.isGoNext)
            if (it.totalCountries > 0 && it.totalCountries == (it.wrongCountries + it.correctCountries)) {
                animateEnd()
            }
            if (it.forceExit) {
                Handler(Looper.getMainLooper()).postDelayed({
                    requireActivity().supportFragmentManager.popBackStack()
                }, Constants.DEFAULT_AWAIT_TIME)
            }
        }.launchIn(this)
    }


    private fun animateEnd() {
        animateView(konfettiView = mBinder.viewKonfetti)
    }

    /**
     *
     */
    private fun updateScoreValues(cAmount: Int, correct: Int, wrong: Int) {
        with(mBinder.scoreInclude) {
            totalTV.text = resources.getString(R.string.quiz_total, (correct + wrong), cAmount)
            correctTV.text = resources.getString(R.string.quiz_correct, correct)
            incorrectTV.text = resources.getString(R.string.quiz_wrong, wrong)

            androidx.transition.TransitionManager.beginDelayedTransition(quizScoreMainView, ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_KEEP))
        }
    }

    /**
     *
     */
    private fun addObserver(isGoNext: Boolean) {
        if (isGoNext) {
            mBinder.overlayView.visibility = View.VISIBLE
            mBinder.infoIcon.visibility = View.VISIBLE
            AnimationHelper.animateSpringY(mBinder.nextBtn, 0F)
        } else {
            mBinder.nextBtn.animate().translationY(1000F).setDuration(0L).start()
            mBinder.overlayView.visibility = View.GONE
            mBinder.infoIcon.visibility = View.GONE
        }
    }

    /**
     *
     */
    private fun setFlag(item: FlagQuizMainObject) {
        with(provider) {
            val rId = item.flagReference.getResurceFromRaw
            ResourcesCompat.getDrawable(resources, rId, requireContext().theme)?.let { drawable ->
                val (height, width) = drawable.toBitmap().toFlagDimentions
                mBinder.flagContainer.layoutParams.height = height
                mBinder.flagContainer.layoutParams.width = width
                mBinder.flagIV.scaleType = ImageView.ScaleType.FIT_XY
                mBinder.flagIV.setImageDrawableWithAnimation(drawable)
            } ?: kotlin.run {
                mBinder.flagIV.setImageResource(rId)
            }
        }
    }

    /**
     *
     */
    private fun generateAdapter() {
        with(mBinder.recycler) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = FlagsAdapter()
            itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
            GravitySnapHelper(Gravity.TOP).attachToRecyclerView(this)
        }
    }

    /**
     *
     */
    private fun setList(item: FlagQuizMainObject) {
        (mBinder.recycler.adapter as FlagsAdapter).submitList(item.options.toImmutableList())
    }

    /**
     *
     */
    override fun onHandlerClicked(view: View) {
        when (view.id) {
            R.id.nextBtn -> {
                countryViewModel.nextQuiz()
            }
            R.id.infoIcon -> {
                executeWebCall()
            }
        }
    }

    /**
     *
     */
    private fun executeWebCall() {
        val searchCriteria = "${buildBaseUrl()}${countryViewModel.getCountryName()}"
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(searchCriteria))
        requireActivity().startActivity(Intent.createChooser(webIntent, requireContext().getString(R.string.wikipedia_country_chooser_text)))
    }

    /**
     *
     */
    private fun buildBaseUrl() = "${Prefs.getAppLanguage().toAbbreviation()}${Constants.WIKIPEDIA_COUNTRY_BASE_URL}"

}