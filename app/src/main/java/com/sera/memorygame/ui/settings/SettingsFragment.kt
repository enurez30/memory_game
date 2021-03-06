@file:Suppress("UNCHECKED_CAST")

package com.sera.memorygame.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.sera.memorygame.R
import com.sera.memorygame.database.datastore.AppPreferences
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
class SettingsFragment : BaseFragment() {

    private lateinit var mBinder: SettingsFragmentBinding

    @Inject
    lateinit var userViewModel: UserViewModel

    @Inject
    lateinit var appPrefs: AppPreferences

    @Inject
    lateinit var provider: ResourcesProvider

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
        addObservers()
        lifecycleScope.launchWhenCreated {
            flow { emitAll(test()) }.collect()
        }
    }

    /**
     *
     */
    private fun addObservers() {
        userViewModel.getUserInSession().asLiveData().observe(viewLifecycleOwner,{
            generateUserView()
        })
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
        mBinder.tLayout.themeName.text =provider.getString(reference =  Prefs.getThemeName())
    }

    /**
     *
     */
    private suspend fun test(): Flow<Unit> = flow {
        val flowA = appPrefs.appLanguage
        val flowB = getLanguageViewData()
        flowA.combine(flowB) { lang, list ->
            val appLanguage = when (lang) {
                "iw" -> provider.getString(reference = "hebrew")
                else -> provider.getString(reference = "english")
            }

            val adapter = IObjectAutocompleteAdapter(requireContext(), list, this@SettingsFragment)
            with(mBinder.languageLayout.textField.editText as? AutoCompleteTextView) {
                this?.setAdapter(adapter)
                this?.setText(appLanguage)
                this?.setOnItemClickListener { adapterView, _, i, _ ->
                    println()
                    (adapterView.adapter.getItem(i) as? LanguageObject)?.let { item ->
                        this.setText(item.name)
                        lifecycleScope.launch {
                            Prefs.setAppLanguage(appLanguage = item.languageRefrence)
                            appPrefs.saveAppLanguage(item.languageRefrence)
                            delay(500)
                            requireActivity().recreate()
                        }
                    }
                }
            }

        }.collect()
    }


    /**
     *
     */
    private fun getLanguageViewData(): Flow<ArrayList<IObject>> = flow {
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
        emit(list)
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