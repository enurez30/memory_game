package com.sera.memorygame.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.sera.memorygame.MessageEvent
import com.sera.memorygame.R
import com.sera.memorygame.databinding.BaseDialogFragmentLayoutBinding
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.interfaces.OnBackPressedListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseDialogFragment : DialogFragment(), Handlers {

    private lateinit var mBinder: BaseDialogFragmentLayoutBinding
    private var mListener: OnBackPressedListener? = null

    /**
     *
     */
    @SuppressLint("NewApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = object : Dialog(requireContext(), theme) {
            override fun onBackPressed() {
                mListener?.onBackPressed()
            }
        }
        dialog.window?.setWindowAnimations(getStyle())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        mListener = getListener()
        return dialog
    }

    /**
     *
     */
    override fun onStart() {
        super.onStart()
        val params = dialog?.window?.attributes
        if (getWidth() != null) {
            params?.width = getWidth()
        }
        if (getHeight() != null) {
            params?.height = getHeight()
        }
        dialog?.window?.attributes = params as android.view.WindowManager.LayoutParams
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    /**
     *
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinder = DataBindingUtil.inflate(inflater, R.layout.base_dialog_fragment_layout, container, false)
        mBinder.childContainer.addView(getChildView(container = container), LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
        return mBinder.root
    }

    /**
     *
     */
    fun showActionMessage(content: String) {
        Toast.makeText(requireContext(), content, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.CENTER, 0, 0)
        }.show()
    }

    /**
     *
     */
    override fun onHandlerClicked(view: View) {
        delegateHandlerClick(view = view)
    }

    /**
     *
     */
    override fun onHandleClickedWithPosition(view: View, position: Int) {
        delegateHandlerClickWithPosition(view = view, position = position)
    }

    /**
     *
     */
    override fun onLongClicked(view: View, position: Int?): Boolean {
        return delegateHandlerLongClick(view = view, position = position)
    }

    /**
     *
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    fun onEvent(event: MessageEvent) {
        delegateEventResult(event = event)
    }

    /**
     *
     */
    protected abstract fun getChildView(container: ViewGroup?): View

    open fun getStyle(): Int = R.style.PopUpDialogAnimation
    open fun getWidth(): Int? = (requireContext().resources.displayMetrics.widthPixels / 1.2).toInt()
    open fun getHeight(): Int? = null
    open fun getListener(): OnBackPressedListener? = null
    open fun delegateHandlerClick(view: View) {}
    open fun delegateHandlerClickWithPosition(view: View, position: Int) {}
    open fun delegateHandlerLongClick(view: View, position: Int?): Boolean = false
    open fun delegateEventResult(event: MessageEvent) {}
}