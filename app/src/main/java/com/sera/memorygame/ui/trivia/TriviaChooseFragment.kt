package com.sera.memorygame.ui.trivia

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import com.sera.memorygame.R
import com.sera.memorygame.databinding.FragmentTriviaChooseBinding
import com.sera.memorygame.network.model.TriviaCategoryModel
import com.sera.memorygame.providers.ResourcesProvider
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.ui.MainActivity
import com.sera.memorygame.ui.adapter.IObjectAutocompleteAdapter
import com.sera.memorygame.viewModel.TriviaViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
class TriviaChooseFragment : BaseFragment() {
    private lateinit var mBinder: FragmentTriviaChooseBinding

    @Inject
    lateinit var viewModel: TriviaViewModel

    /**
     *
     */
    companion object {
        fun newInstance() = TriviaChooseFragment()
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
        mBinder = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.fragment_trivia_choose, container, false)
        return mBinder.root
    }

    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
        prepareView()
        addObservers()
        addEditTextListeners()
        generateDifficultyView()
    }

    /**
     *
     */
    private fun addObservers() {
        viewModel.getTriviaCategories.observe(viewLifecycleOwner, {
            it?.let { list ->
                if (list.isNotEmpty()) {
                    // populate automplete
                    generateCategoryView(list = list)
                } else {
                    // something went wrong
                    println("TriviaChooseFragment error")
                }
            } ?: kotlin.run {
                viewModel.getTriviaCategories()
            }
        })
    }

    /**
     *
     */
    private fun prepareView() {
        with(mBinder) {
            textFieldCategory.isActivated = false
            overlay.visibility = View.GONE
        }
    }

    /**
     *
     */
    private fun generateDifficultyView() {
        val items = listOf("Any Difficulty", "Easy", "Medium", "Hard")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        with(mBinder.textFieldDifficulty.editText as? AutoCompleteTextView) {
            this?.setAdapter(adapter)
            this?.setText(adapter.getItem(0), false)
        }
    }

    /**
     *
     */
    private fun generateCategoryView(list: ArrayList<TriviaCategoryModel>) {
        mBinder.textFieldCategory.isActivated = true

        val adapter = IObjectAutocompleteAdapter(requireContext(), list, this@TriviaChooseFragment)
        with(mBinder.textFieldCategory.editText as? AutoCompleteTextView) {
            this?.setAdapter(adapter)
            this?.setText(adapter.getItem(0).name)
            this?.setOnItemClickListener { adapterView, _, i, _ ->
                println()
                (adapterView.adapter.getItem(i) as? TriviaCategoryModel)?.let { item ->
                    this.setText(item.name)
                    this.tag = item.id
                }
            }
        }
    }

    /**
     *
     */
    override fun onHandlerClicked(view: View) {
        when (view.id) {
            R.id.confirmBtn -> {
                if (validateForm()) {
                    with(mBinder) {
                        viewModel.buildRequest(
                            amount = textFieldNumberOfQuestions.editText?.text.toString().toInt(),
                            category = (textFieldCategory.editText?.tag as? Int) ?: -1,
                            diff = textFieldDifficulty.editText?.text.toString()
                        ).let {
                            viewModel.getTriviaQuestions(map = it)
                        }
                    }
                }
            }
        }
    }

    /**
     *
     */
    private fun validateForm(): Boolean {
        val provider = ResourcesProvider(context = requireContext())
        with(mBinder) {
            if (textFieldNumberOfQuestions.editText?.text.toString().isEmpty()) {
                textFieldNumberOfQuestions.error = provider.getString(reference = "empty_field")
                return false
            }
        }
        return true
    }

    /**
     *
     */
    private fun addEditTextListeners() {
        mBinder.textFieldNumberOfQuestions.setEndIconOnClickListener {
            mBinder.textFieldNumberOfQuestions.editText?.setText("")
            mBinder.textFieldNumberOfQuestions.error = null
        }
    }
}