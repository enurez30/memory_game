package com.sera.memorygame.ui.flag_quiz

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.sera.memorygame.R
import com.sera.memorygame.database.datastore.AppPreferences
import com.sera.memorygame.database.model.FlagQuizMainObject
import com.sera.memorygame.database.model.FlagQuizSingleObject
import com.sera.memorygame.database.model.IObject
import com.sera.memorygame.databinding.FlagQuizFragmentBinding
import com.sera.memorygame.event.MessageEvent
import com.sera.memorygame.providers.ResourcesProvider
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.ui.MainActivity
import com.sera.memorygame.ui.adapter.BaseRecyclerViewAdapter
import com.sera.memorygame.ui.adapter.CommonAdapter
import com.sera.memorygame.utils.AnimationHelper
import com.sera.memorygame.utils.Constants
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


@ExperimentalCoroutinesApi
class FlagQuizFragment : BaseFragment() {
    private lateinit var mBinder: FlagQuizFragmentBinding


    @Inject
    lateinit var appPrefs: AppPreferences

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
        fun newInstance(entity: FlagQuizMainObject) = FlagQuizFragment().apply {
            arguments = Bundle().apply {
                this.putSerializable("entity", entity)
            }
        }
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
        mBinder = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.flag_quiz_fragment, container, false)
        return mBinder.root
    }

    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
        addObserver()
        setFlag()
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
                mBinder.infoIcon.visibility = View.VISIBLE
                AnimationHelper.animateSpringY(mBinder.newxBtn, 0F)
            } else {
                mBinder.newxBtn.animate().translationY(1000F).setDuration(0L).start()
                mBinder.overlayView.visibility = View.GONE
                mBinder.infoIcon.visibility = View.GONE
            }
        })
    }

    /**
     *
     */
    private fun setFlag() {
        (requireArguments().getSerializable("entity") as? FlagQuizMainObject?)?.let { fqm ->
            with(ResourcesProvider(context = requireContext())) {

                val rId = this.getResurceFromRaw(fName = fqm.flagReference)
                ResourcesCompat.getDrawable(resources, rId, requireContext().theme)?.toBitmap()?.let {
                    val ratio = it.width.toDouble() / it.height.toDouble()
                    mBinder.flagContainer.layoutParams.height = (it.height * 2)
                    mBinder.flagContainer.layoutParams.width = ((it.height * 2) * ratio).toInt()
                    mBinder.flagIV.scaleType = ImageView.ScaleType.FIT_XY
                    mBinder.flagIV.setImageBitmap(it)
                } ?: kotlin.run {
                    mBinder.flagIV.setImageResource(rId)
                }

            }
        }
    }

    /**
     *
     */
    private fun generateAdapter() {
        with(mBinder.recycler) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CommonAdapter(handlers = this@FlagQuizFragment)
            itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
            GravitySnapHelper(Gravity.TOP).attachToRecyclerView(this)
        }
    }

    /**
     *
     */
    private fun setList() {
        (requireArguments().getSerializable("entity") as? FlagQuizMainObject?)?.let { fqm ->
            ArrayList<IObject>().apply {
                this.addAll(fqm.options)
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
                    (this.getItemByPosition(position = position) as? FlagQuizSingleObject?)?.let {

                        if (keyLiveData.value?.isEmpty() == true) {
                            val key = if (it.isRight) {
                                "correct"
                            } else {
                                "wrong"
                            }
                            keyLiveData.value = key
                        }

                        this.items.mapIndexed { index, iObject ->
                            (iObject as? FlagQuizSingleObject?)?.let { fqo ->
                                fqo.animate = true
                            }
                            this.notifyItemChanged(index)
                        }
                        sendEvent(key = keyLiveData.value ?: "", message = (requireArguments().getSerializable("entity") as? FlagQuizMainObject?)?.countryId ?: "")
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
            R.id.newxBtn -> {
                sendEvent(key = "next", message = (requireArguments().getSerializable("entity") as? FlagQuizMainObject?)?.countryId ?: "")
            }
            R.id.infoIcon -> {
                lifecycleScope.launch {
                    executeWebCall()
                }
            }
        }
    }

    /**
     *
     */
    private suspend fun executeWebCall() {
        buildBaseUrl().collect { baseUrl ->
            val searchCriteria = "$baseUrl${getCountryName()}"
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(searchCriteria))
            requireActivity().startActivity(Intent.createChooser(webIntent, requireContext().getString(R.string.wikipedia_country_chooser_text)))
        }
    }

    /**
     *
     */
    private suspend fun buildBaseUrl() = flow {
        appPrefs.appLanguage.collect { result ->
            val prefix = result?.let {
                when(it){
                    "iw"->"https://he."
                    else->"https://$it."
                }
            } ?: kotlin.run {
                "https://en."
            }

            emit("$prefix${Constants.WIKIPEDIA_COUNTRY_BASE_URL}")
        }
    }

    /**
     *
     */
    private fun getCountryName(): String {
        (mBinder.recycler.adapter as BaseRecyclerViewAdapter).items.map {
            if ((it as? FlagQuizSingleObject?)?.isRight == true) {
                return it.flagName
            }
        }
        return ""
    }

    /**
     *
     */
    private fun sendEvent(key: String, message: String) {
        val event = MessageEvent().apply {
            this.reciever = FlagQuizContainerFragment::class.java.simpleName
            this.key = key
            this.message = message
        }
        EventBus.getDefault().post(event)
    }
}