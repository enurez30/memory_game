package com.sera.memorygame.custom

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.sera.memorygame.R
import com.sera.memorygame.database.model.MemoryViewObject
import com.sera.memorygame.extentions.resolveColorAttr
import com.sera.memorygame.ui.memory.MemoryCardAnimationState
import com.sera.memorygame.utils.Utils
import com.yayandroid.rotatable.Rotatable


@SuppressLint("ViewConstructor")
class MemoryCardView : CardView {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, android.R.attr.editTextStyle) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private var rotatable: Rotatable? = null

//    init {
//        init()
//    }

    @SuppressLint("ResourceType")
    private fun init() {

//        this.id = View.generateViewId()
        this.radius = Utils.dpToPx(3).toFloat()
        this.maxCardElevation = 3F
        this.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorLightGrey))
//        this.tag = memoryViewObject.id

//        this.layoutParams = LayoutParams().apply {
//            this.setMargins(8, 8, 8, 8)
//        }


//        val targetImg = ImageView(context).apply {
//            this.id = View.generateViewId()
//            (memoryViewObject.frontResource as? Drawable)?.let {
//                this.setImageDrawable(it)
//            }
//        }
//
//        with(targetImg) {
//            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
//            scaleType = ImageView.ScaleType.FIT_CENTER
//        }
//
//        this.addView(targetImg)
//
//        val backCardImg = ImageView(context).apply {
//            this.id = View.generateViewId()
//            when (val resource = memoryViewObject.backResource) {
//                is Drawable -> {
//                    this.setImageDrawable(resource)
//                }
//                is Int -> {
//                    this.setBackgroundColor(context.resolveColorAttr(colorAttr = resource))
//                }
//            }
//        }
//
//        with(backCardImg) {
//            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
//            scaleType = ImageView.ScaleType.CENTER_CROP
//        }
//
//        this.addView(backCardImg)
//
//        buildImageRotation(viewToRotate = this, frontViewId = targetImg.id, backViewId = backCardImg.id)


//        backCardImg.setOnClickListener {
//            callback.onHandlerClicked(view = this)
//            onRotationClick(degree = 180F)
//        }
    }


    @SuppressLint("ResourceType")
    fun generate(item: MemoryViewObject) {

        this.tag = item
        this.layoutParams = LayoutParams(item.width.toInt(), item.height.toInt()).apply {
            this.setMargins(8, 8, 8, 8)
        }

        val targetImg = ImageView(context).apply {
            this.id = View.generateViewId()
            (item.frontResource as? Drawable)?.let {
                this.setImageDrawable(it)
            }
        }

        with(targetImg) {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        this.addView(targetImg)

        val backCardImg = ImageView(context).apply {
            this.id = View.generateViewId()
            when (item.backResource) {
                is Drawable -> {
                    this.setImageDrawable(item.backResource)
                }
                is Int -> {
                    this.setBackgroundColor(context.resolveColorAttr(colorAttr = item.backResource))
                }
            }
        }

        with(backCardImg) {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        this.addView(backCardImg)

        buildImageRotation(viewToRotate = this, frontViewId = targetImg.id, backViewId = backCardImg.id)

        backCardImg.setOnClickListener {
            item.onClick(Pair(this, item))
//            onRotationClick(degree = 180F)
        }
//        requestLayout()
    }

    /**
     *
     */
    private fun onRotationClick(degree: Float) {
        val item = this.tag as? MemoryViewObject
        rotatable?.rotate(Rotatable.ROTATE_Y, degree, 500, object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
                println("TEST_TEST: onAnimationStart")
                item?.onAnimationState?.let { it(MemoryCardAnimationState.ANIMATION_IN_PROGRESS) }
            }

            override fun onAnimationEnd(p0: Animator) {
                println("TEST_TEST: onAnimationEnd")
                item?.onAnimationState?.let { it(MemoryCardAnimationState.ANIMATION_ENDED) }
            }

            override fun onAnimationCancel(p0: Animator) {

            }

            override fun onAnimationRepeat(p0: Animator) {

            }

        })
    }

    /**
     *
     */
    private fun buildImageRotation(viewToRotate: View, frontViewId: Int, backViewId: Int) {
        val builder = Rotatable.Builder(viewToRotate)
            .sides(backViewId, frontViewId)
            .direction(Rotatable.ROTATE_Y)
            .rotationDistance(1000F)

//        if (rotatable != null) {
        rotatable?.drop()
//        }
        rotatable = builder.build()
    }


    /**
     *
     */
    fun reset() {
        onRotationClick(degree = 0F)
    }

    /**
     *
     */
    fun showCard() {
        onRotationClick(degree = 180F)
    }
}