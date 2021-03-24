@file:Suppress("UNCHECKED_CAST")

package com.sera.memorygame.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.sera.memorygame.R
import com.sera.memorygame.database.model.IObject
import com.sera.memorygame.database.model.LanguageObject
import com.sera.memorygame.databinding.SettingsFragmentBinding
import com.sera.memorygame.extentions.themeColor
import com.sera.memorygame.providers.ResourcesProvider
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.ui.MainActivity
import com.sera.memorygame.ui.adapter.IObjectAutocompleteAdapter
import com.sera.memorygame.ui.dialog.AppThemeDialog
import com.sera.memorygame.ui.dialog.UserDialog
import com.sera.memorygame.utils.Prefs
import com.sera.memorygame.viewModel.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import javax.inject.Inject


@ExperimentalCoroutinesApi
class SettingsFragment : BaseFragment() {

    private lateinit var mBinder: SettingsFragmentBinding

    @Inject
    lateinit var userViewModel: UserViewModel

    /**
     *
     */
    companion object {
        fun newInstance() = SettingsFragment()
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
            merge(
                flow { emit(addObservers()) },
                flow { emit(generateLanguageView()) }
            ).collect()
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
    private suspend fun generateLanguageView() {
        println()
        getLanguageViewData().collect {
            val (lang, list) = it
            val adapter = IObjectAutocompleteAdapter(requireContext(), list as ArrayList<IObject>, this)
            with(mBinder.languageLayout.textField.editText as? AutoCompleteTextView) {
                this?.setAdapter(adapter)
                this?.setText((lang as String))
                this?.setOnItemClickListener { adapterView, _, i, _ ->
                    println()
                    (adapterView.adapter.getItem(i) as? LanguageObject)?.let { item ->
                        this.setText(item.name)
                        Prefs.setAppLanguage(appLanguage = item.languageRefrence)
                        requireActivity().recreate()
                    }
                }
            }
        }

    }

    /**
     *
     */
    private fun getLanguageViewData(): Flow<List<Any>> = flow {
        val provider = ResourcesProvider(context = requireContext())
        val list = ArrayList<IObject>().apply {
            this.add(
                LanguageObject(
                    iconRefrerence = "gb",
                    languageRefrence = "en",
                    name = provider.getString(reference = "english")
                )
            )
            this.add(
                LanguageObject(
                    iconRefrerence = "il",
                    languageRefrence = "iw",
                    name = provider.getString(reference = "hebrew")
                )
            )
        }
        val appLanguage = when (Prefs.getAppLanguage()) {
            "en" -> provider.getString(reference = "english")
            else -> provider.getString(reference = "hebrew")
        }

        emit(listOf<Any>(appLanguage, list))
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