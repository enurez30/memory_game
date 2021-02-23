package com.sera.memorygame.ui.memory

import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.sera.memorygame.BaseRecyclerViewAdapter
import com.sera.memorygame.R
import com.sera.memorygame.databinding.FragmentMemoryBinding
import com.sera.memorygame.factory.MemoryViewModelFactory
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.model.IObject
import com.sera.memorygame.model.SizeViewObject
import com.sera.memorygame.ui.BaseFragment
import com.sera.memorygame.view.MemoryCardView
import com.sera.memorygame.viewModel.MemoryViewModel
import java.util.*
import kotlin.collections.ArrayList

class MemoryFragment : BaseFragment(), Handlers {

    private lateinit var mBinder: FragmentMemoryBinding

    /**
     *
     */
    private val viewModel: MemoryViewModel by viewModels {
        MemoryViewModelFactory(
            context = requireContext(),
            jsonRef = requireArguments().getString("json_ref", ""),
            sizeViewObject = requireArguments().getSerializable("sizeObj") as SizeViewObject
        )
    }

    /**
     *
     */
    companion object {
        fun newInstance(jsonRef: String, sizeObj: SizeViewObject) = MemoryFragment().apply {
            arguments = Bundle().apply {
                this.putString("json_ref", jsonRef)
                this.putSerializable("sizeObj", sizeObj)
            }
        }
    }

    /**
     *
     */
    override fun getChildView(container: ViewGroup?): View {
        mBinder = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.fragment_memory, container, false)
        return mBinder.root
    }

    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinder.handlers = this
        addObservers()
        generateAdapter()
        mBinder.container.post {
            viewModel.generateMemoryViewObject(
                memoryObjeList = viewModel.generateMemoryObjects(
                    containerW = mBinder.container.width,
                    containerH = mBinder.container.height
                ),
                handlers = this@MemoryFragment
            ).let { list ->
                (mBinder.recycler.adapter as BaseRecyclerViewAdapter).setList(list = ArrayList<IObject>().run {
                    this.addAll(list)
                    this
                })
                animateLayout()
            }
        }

    }

    /**
     *
     */
    private fun animateLayout() {
        mBinder.recycler.layoutAnimation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
    }

    /**
     *
     */
    private fun generateAdapter() {
        val sizeViewObject = requireArguments().getSerializable("sizeObj") as SizeViewObject
        with(mBinder.recycler) {
            layoutManager = GridLayoutManager(requireContext(), sizeViewObject.xAxis)
            adapter = CommonAdapter()
        }
    }

    /**
     *
     */
    private fun addObservers() {
        viewModel.updatePair.observe(viewLifecycleOwner, {
            if (it.first != null && it.second != null) {
                Toast.makeText(requireContext(), "${it.first?.tag} - ${it.second?.tag} ", Toast.LENGTH_SHORT).show()
                mBinder.overlay.visibility = View.VISIBLE
                if (it.first?.tag == it.second?.tag) {
                    val prev = viewModel.getControlValue.value ?: 0
                    viewModel.getControlValue.value = prev + 2

                    Toast.makeText(requireContext(), "Right match!", Toast.LENGTH_SHORT).show()
                    viewModel.updatePair.value = Pair(null, null)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        it.first?.reset()
                        it.second?.reset()
                        viewModel.updatePair.value = Pair(null, null)
                    }, 1000)
                }


            } else {
                mBinder.overlay.visibility = View.GONE
            }
        })
        viewModel.getControlValue.observe(viewLifecycleOwner, {
            it?.let {
                if (it == viewModel.memoryListValue.value) {
                    // game over
                    Toast.makeText(requireContext(), "Great Game!!", Toast.LENGTH_LONG).show()
                    Handler(Looper.myLooper() ?: Looper.getMainLooper()).postDelayed({
                        requireActivity().onBackPressed()
                    }, 2000)
                }
            } ?: kotlin.run {

            }
        })
    }

    /**
     *
     */
    override fun onHandlerClicked(view: View) {
        when (view.id) {
            R.id.cardView -> {
//                val degree = if (mBinder.childImageBackCard.visibility == View.VISIBLE) {
//                    180F
//                } else {
//                    0F
//                }
//                viewModel.onRotationClick(degree = degree)
            }
            else -> {
                // memory card was clicked
                (view as? MemoryCardView)?.let { cardView ->
                    viewModel.updatePair.value?.let {
                        var pair: Pair<MemoryCardView?, MemoryCardView?> = it
                        when {
                            it.first == null -> {
                                pair = Pair(cardView, null)
                            }
                            it.second == null -> {
                                pair = Pair(it.first, cardView)
                            }
                        }
                        viewModel.updatePair.value = pair
                    }
                }
            }
        }
    }

    /**
     *
     */
    override fun onHandleClickedWithPosition(view: View, position: Int) {
        TODO("Not yet implemented")
    }

    /**
     *
     */
    override fun onLongClicked(view: View, position: Int?): Boolean {
        TODO("Not yet implemented")
    }
}