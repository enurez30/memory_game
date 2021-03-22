package com.sera.memorygame.ui.flag_quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionManager
import com.sera.memorygame.R
import com.sera.memorygame.database.entity.CountryEntity
import com.sera.memorygame.databinding.FragmentFlagQuizContainerBinding
import com.sera.memorygame.event.MessageEvent
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.ui.MainActivity
import com.sera.memorygame.viewModel.CountryViewModel
import com.transitionseverywhere.ChangeText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


class FlagQuizContainerFragment : BaseFragment() {
    private lateinit var mBinder: FragmentFlagQuizContainerBinding

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
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as? MainActivity?)?.mainComponent?.inject(fragment = this)
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
        addObserver()
    }

    /**
     *
     */
    private fun addObserver() {
        countryViewModel.getRemainCountriesLive.observe(viewLifecycleOwner, {
            if (it?.isNotEmpty() == true) {
                lifecycleScope.launch {
                    updateScoreValues()
                }
                setNextItem()
            }
        })

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
                }
            }
            "wrong" -> {
                country?.let {
                    val oldValue = countryViewModel.getWrongCountries().value
                    val newValue = oldValue + 1
                    countryViewModel.setWrongCountries(newValue = newValue)
                }
            }
        }

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
            val totalCountries = getAllCountries().value?.size ?: 0
            val total = totalCountries - (getRemainCountriesLive.value?.size ?: 0)
            val correct = getCorrectCountries().value
            val wrong = getWrongCountries().value

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
            getResult(key = event.key, message = event.message)
        }
    }

}