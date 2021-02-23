package com.sera.memorygame.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.google.android.material.transition.MaterialSharedAxis
import com.sera.memorygame.R
import com.sera.memorygame.databinding.BaseFragmentLayoutBinding
import com.sera.memorygame.interfaces.Handlers

abstract class BaseFragment : Fragment(), FragmentResultListener, Handlers {
    private lateinit var mBinder: BaseFragmentLayoutBinding

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val forward = MaterialSharedAxis(MaterialSharedAxis.X, true)
        enterTransition = forward

        val backward = MaterialSharedAxis(MaterialSharedAxis.X, false)
        returnTransition = backward
    }

    /**
     *
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinder = DataBindingUtil.inflate(inflater, R.layout.base_fragment_layout, container, false)
        mBinder.childContainer.addView(getChildView(container = container), LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
        return mBinder.root
    }

//    /**
//     *
//     */
//    fun addFragmentListener(tag: String) {
//        requireActivity().supportFragmentManager.setFragmentResultListener(tag, viewLifecycleOwner, this)
//    }

    /**
     *
     */
    override fun onFragmentResult(requestKey: String, result: Bundle) {
        delegateOnFragmentResult(requestKey = requestKey, result = result)
    }

    /**
     *
     */
    abstract fun getChildView(container: ViewGroup?): View

    /**
     *
     */
    open fun delegateOnFragmentResult(requestKey: String, result: Bundle) {}


    override fun onHandlerClicked(view: View) {
        delegateHandlerClick(view = view)
    }

    override fun onHandleClickedWithPosition(view: View, position: Int) {
        delegateHandlerClickWithPosition(view = view, position = position)
    }

    override fun onLongClicked(view: View, position: Int?): Boolean {
        return delegateHandlerLongClick(view = view, position = position)
    }

    open fun delegateHandlerClick(view: View) {}
    open fun delegateHandlerClickWithPosition(view: View, position: Int) {}
    open fun delegateHandlerLongClick(view: View, position: Int?): Boolean = false

}