package com.sera.memorygame.ui.trivia

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.sera.memorygame.R
import com.sera.memorygame.databinding.FragmentTriviaContainerBinding
import com.sera.memorygame.event.MessageEvent
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


@ExperimentalCoroutinesApi
class TriviaContainerFragment : BaseFragment() {
    private lateinit var mBinder: FragmentTriviaContainerBinding

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
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as? MainActivity?)?.mainComponent?.inject(fragment = this)
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
        lifecycleScope.launch {
            addObservers()
        }
    }

    /**
     *
     */
    private suspend fun addObservers() {
//        viewModel.getTriviaObjects.observe(viewLifecycleOwner, {
//            it?.let { list ->
//                mBinder.camomileSpinner.visibility = View.GONE
//                // do something
//                if (list.isEmpty()) {
//                    // game over
//                } else {
//                    setNextItem()
//                }
//            } ?: kotlin.run {
//                // make a call
//                mBinder.camomileSpinner.visibility = View.VISIBLE
//                mBinder.camomileSpinner.start()
//                viewModel.getTriviaObjects()
//            }
//        })
        viewModel.getMediator().observe(viewLifecycleOwner, {
            it?.let { list ->
                println("TRIVIA: list size = ${it.size}")
                mBinder.camomileSpinner.visibility = View.GONE
                // do something
                if (list.isEmpty()) {
                    // game over
                    call()
                } else {
                    setNextItem()
                }
            } ?: kotlin.run {
                // make a call
                call()
            }
        })
    }

    /**
     *
     */
    private fun call() {
        mBinder.camomileSpinner.visibility = View.VISIBLE
        mBinder.camomileSpinner.start()
        viewModel.getTriviaObjects()
    }

    /**
     *
     */
    private fun setNextItem() {
        replaceFragment(fragment = TriviaFragment.newInstance(entity = viewModel.getTriviaObject()))
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
            }
        }
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
//            when (event.key) {
//                "next" -> {
            goNext(message = event.message)
//                }
//                else -> getResult(key = event.key, message = event.message)
//            }
        }
    }
}