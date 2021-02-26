package com.sera.memorygame.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.sera.memorygame.R
import com.sera.memorygame.interfaces.Handlers
import com.sera.memorygame.model.MemoryViewObject
import com.sera.memorygame.utils.Utils
import com.yayandroid.rotatable.Rotatable


@SuppressLint("ViewConstructor")
class MemoryCardView constructor(private val mContext: Context, private val memoryViewObject: MemoryViewObject, private var callback: Handlers) : CardView(mContext, null, 0) {
    private var rotatable: Rotatable? = null

    init {
        init()
    }

    private fun init() {

        this.id = View.generateViewId()
        this.radius = Utils.dpToPx(3).toFloat()
        this.maxCardElevation = 3F
        this.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorLightGrey))
        this.tag = memoryViewObject.id

        this.layoutParams = LayoutParams(memoryViewObject.width.toInt(), memoryViewObject.height.toInt()).apply {
            this.setMargins(8, 8, 8, 8)
        }


        val targetImg = ImageView(mContext).apply {
            this.id = View.generateViewId()
            (memoryViewObject.frontResource as? Drawable)?.let {
                this.setImageDrawable(it)
            }
        }

        with(targetImg) {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        this.addView(targetImg)

        val backCardImg = ImageView(mContext).apply {
            this.id = View.generateViewId()
            when (val resource = memoryViewObject.backResource) {
                is Drawable -> {
                    this.setImageDrawable(resource)
                }
                is Int -> {
                    this.setImageDrawable(AppCompatResources.getDrawable(mContext, resource))
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
            callback.onHandlerClicked(view = this)
            onRotationClick(degree = 180F)
        }

    }

    /**
     *
     */
    private fun onRotationClick(degree: Float) {
        rotatable!!.rotate(Rotatable.ROTATE_Y, degree)
    }

    /**
     *
     */
    private fun buildImageRotation(viewToRotate: View, frontViewId: Int, backViewId: Int) {
        val builder = Rotatable.Builder(viewToRotate)
            .sides(backViewId, frontViewId)
            .direction(Rotatable.ROTATE_Y)
            .rotationDistance(1000F)

        if (rotatable != null) {
            rotatable!!.drop()
        }
        rotatable = builder.build()
    }


    /**
     *
     */
    fun reset() {
        onRotationClick(degree = 0F)
    }

}