package com.sera.memorygame.ui.trivia

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.sera.memorygame.R
import com.sera.memorygame.database.entity.TriviaEntity
import com.sera.memorygame.database.model.IObject
import com.sera.memorygame.database.model.TriviaAnswerSingleObject
import com.sera.memorygame.databinding.TriviaFragmentBinding
import com.sera.memorygame.event.MessageEvent
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.ui.adapter.BaseRecyclerViewAdapter
import com.sera.memorygame.ui.adapter.CommonAdapter
import com.sera.memorygame.utils.AnimationHelper
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

@ExperimentalCoroutinesApi
class TriviaFragment : BaseFragment() {
    private lateinit var mBinder: TriviaFragmentBinding

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
        fun newInstance(entity: TriviaEntity) = TriviaFragment().apply {
            arguments = Bundle().apply {
                this.putSerializable("entity", entity)
            }
        }
    }


    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.trivia_fragment, container, false)
        return mBinder.root
    }

    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
        addObserver()
        generateView()
        generateAdapter()
        lifecycleScope.launch {
            delay(100)
            setList()
        }
    }

    /**
     *
     */
    private fun addObserver() {
        keyLiveData.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                mBinder.overlayView.visibility = View.VISIBLE
                AnimationHelper.animateSpringY(mBinder.nextBtn, 0F)
            } else {
                mBinder.nextBtn.animate().translationY(1000F).setDuration(0L).start()
                mBinder.overlayView.visibility = View.GONE
            }
        })
    }

    /**
     *
     */
    private fun generateView() {
        (requireArguments().getSerializable("entity") as? TriviaEntity?)?.let { trivia ->
            mBinder.questionTV.text = trivia.question
            mBinder.categoryTV.text = trivia.category
        }
    }

    /**
     *
     */
    private fun generateAdapter() {
        with(mBinder.recycler) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CommonAdapter(handlers = this@TriviaFragment)
            itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
            GravitySnapHelper(Gravity.TOP).attachToRecyclerView(this)
        }
    }

    /**
     *
     */
    private fun setList() {
        (requireArguments().getSerializable("entity") as? TriviaEntity?)?.let { trivia ->
            ArrayList<IObject>().apply {
                this.add(
                    TriviaAnswerSingleObject(
                        answer = trivia.correctAnswer,
                        isRight = true
                    )
                )
                trivia.incorrectAnswers.map {
                    this.add(
                        TriviaAnswerSingleObject(
                            answer = it,
                        )
                    )
                }
            }.also {
                it.shuffle()
            }.run {
                (mBinder.recycler.adapter as BaseRecyclerViewAdapter).setList(list = this)
            }
        }
    }

    /**
     *
     */
    override fun onHandleClickedWithPosition(view: View, position: Int) {
        when (view.id) {
            R.id.mainView -> {
                with(mBinder.recycler.adapter as BaseRecyclerViewAdapter) {
                    (this.getItemByPosition(position = position) as? TriviaAnswerSingleObject?)?.let {

                        if (keyLiveData.value?.isEmpty() == true) {
                            val key = if (it.isRight) {
                                "correct"
                            } else {
                                "wrong"
                            }
                            keyLiveData.value = key
                        }

                        this.items.mapIndexed { index, iObject ->
                            (iObject as? TriviaAnswerSingleObject?)?.let { fqo ->
                                fqo.animate = true
                            }
                            this.notifyItemChanged(index)
                        }
                        //sendEvent(key = keyLiveData.value ?: "", message = (requireArguments().getSerializable("entity") as? TriviaEntity?)?.id ?: "")
                    }
                }
            }
        }
    }

    /**
     *
     */
    override fun onHandlerClicked(view: View) {
        when (view.id) {
            R.id.nextBtn -> {
                sendEvent(key = "next", message = (requireArguments().getSerializable("entity") as? TriviaEntity?)?.id ?: "")
            }
        }
    }

    /**
     *
     */
    private fun sendEvent(key: String, message: String) {
        val event = MessageEvent().apply {
            this.reciever = TriviaContainerFragment::class.java.simpleName
            this.key = key
            this.message = message
        }
        EventBus.getDefault().post(event)
    }
}