package com.sera.memorygame.ui.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.sera.memorygame.R
import com.sera.memorygame.database.model.AppThemeObject
import com.sera.memorygame.database.model.IObject
import com.sera.memorygame.databinding.AppThemeDialogLayoutBinding
import com.sera.memorygame.ui.adapter.BaseRecyclerViewAdapter
import com.sera.memorygame.ui.adapter.CommonAdapter
import com.sera.memorygame.utils.Prefs
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class AppThemeDialog : BaseDialogFragment() {

    private lateinit var mBinder: AppThemeDialogLayoutBinding

    /**
     *
     */
    companion object {
        fun newInstance() = AppThemeDialog()
    }

    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = AppThemeDialogLayoutBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        return mBinder.root
    }

    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
        generateAdapter()
        setList()
    }

    /**
     *
     */
    private fun generateAdapter() {
        with(mBinder.recycler) {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = CommonAdapter(handlers = this@AppThemeDialog)
            itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
            GravitySnapHelper(Gravity.TOP).attachToRecyclerView(this)
        }
    }

    /**
     *
     */
    private fun setList() {
        val list = ArrayList<IObject>().apply {
            this.add(
                AppThemeObject(
                    title = "Pink",
                    mainColor = ContextCompat.getColor(requireContext(), R.color.pink_200),
                    secondaryColor = ContextCompat.getColor(requireContext(), R.color.pink_600Dark),
                    themeRes = R.style.Theme_Pink
                )
            )
            this.add(
                AppThemeObject(
                    title = "Red",
                    mainColor = ContextCompat.getColor(requireContext(), R.color.red_700),
                    secondaryColor = ContextCompat.getColor(requireContext(), R.color.teal_900Dark),
                    themeRes = R.style.Theme_Red
                )
            )
            this.add(
                AppThemeObject(
                    title = "Blue",
                    mainColor = ContextCompat.getColor(requireContext(), R.color.blue_700),
                    secondaryColor = ContextCompat.getColor(requireContext(), R.color.light_blue300Dark),
                    themeRes = R.style.Theme_Blue
                )
            )
            this.add(
                AppThemeObject(
                    title = "Purple",
                    mainColor = ContextCompat.getColor(requireContext(), R.color.purple_400),
                    secondaryColor = ContextCompat.getColor(requireContext(), R.color.deep_purple_500Dark),
                    themeRes = R.style.Theme_Purple
                )
            )
            this.add(
                AppThemeObject(
                    title = "Grey",
                    mainColor = ContextCompat.getColor(requireContext(), R.color.grey_200),
                    secondaryColor = ContextCompat.getColor(requireContext(), R.color.grey_800Dark),
                    themeRes = R.style.Theme_Grey
                )
            )
        }
        (mBinder.recycler.adapter as BaseRecyclerViewAdapter).setList(list = list)
    }

    /**
     *
     */
    override fun onHandleClickedWithPosition(view: View, position: Int) {
        when (view.id) {
            R.id.mainView -> {
                ((mBinder.recycler.adapter as BaseRecyclerViewAdapter).getItemByPosition(position = position) as? AppThemeObject)?.let {
                    Prefs.setTheme(themeRes = it.themeRes)
                    Prefs.setThemeName(name = it.title)
                    requireActivity().recreate()
                }
            }
        }
    }

    /**
     *
     */
    override fun onHandlerClicked(view: View) {
        when (view.id) {
            R.id.cancelBtn -> {
                dismiss()
            }
        }
    }
}