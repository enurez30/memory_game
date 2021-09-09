package com.sera.memorygame.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.sera.memorygame.R
import com.sera.memorygame.databinding.BaseFragmentLayoutBinding
import com.sera.memorygame.event.MessageEvent
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.utils.Constants
import com.sera.memorygame.utils.Utils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import org.greenrobot.eventbus.EventBus

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
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
    fun navigate(direction: NavDirections) {
        findNavController().navigate(direction)
    }

    /**
     *
     */
    override fun onResume() {
        super.onResume()
        sendEvent(msg = this::class.java.simpleName)
    }

    /**
     *
     */
    override fun onFragmentResult(requestKey: String, result: Bundle) {
        delegateOnFragmentResult(requestKey = requestKey, result = result)
    }

    /**
     *
     */
    fun animateView(konfettiView: KonfettiView) {
        konfettiView.build()
            .addColors(Utils.getRandomColors())
            .setDirection(0.0, 365.0)
            .setSpeed(2f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(1500L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(-50f, konfettiView.width + 50f, -50f, -50f)
            .streamFor(1000, 3500L)

    }

    /**
     *
     */
    fun animateShowKonfetti(konfettiView: KonfettiView) {
        konfettiView.build()
            .addColors(Utils.getRandomColors(quantity = 2))
            .setDirection(0.0, 365.0)
            .setSpeed(2f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(1000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(-50f, konfettiView.width + 50f, -50f, -50f)
            .streamFor(500, 700L)
    }

    /**
     *
     */
    private fun sendEvent(msg: String) {
        val event = MessageEvent().apply {
            this.reciever = BaseActivity::class.java.simpleName
            this.key = Constants.EVENT_UPDATE_TITLE
            this.message = msg
        }
        EventBus.getDefault().postSticky(event)
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