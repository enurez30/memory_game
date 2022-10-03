package com.sera.memorygame.ui.flag_quiz

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.widget.ImageView
import com.sera.memorygame.utils.orientation
import com.sera.memorygame.utils.screenHeight
import com.sera.memorygame.utils.screenWidth

val Bitmap.toFlagDimentions: List<Int>
    get() {
        val maxWidth = screenWidth * orientation.widthOrientation
        val maxHeight = (screenHeight * orientation.heightOrientation).toInt()
        val ratio = this.width.toDouble() / this.height.toDouble()
        val newWidth = if ((maxHeight * ratio) <= maxWidth) {
            (maxHeight * ratio).toInt()
        } else {
            maxWidth.toInt()
        }
        return listOf(maxHeight, newWidth)
    }

val Int.widthOrientation: Double
    get() {
        return if (this == Configuration.ORIENTATION_LANDSCAPE) {
            0.5
        } else {
            0.75
        }
    }

val Int.heightOrientation: Double
    get() {
        return if (this == Configuration.ORIENTATION_LANDSCAPE) {
            0.4
        } else {
            0.25
        }
    }

fun ImageView.setImageDrawableWithAnimation(drawable: Drawable, duration: Int = 300) {
    val currentDrawable = getDrawable()
    if (currentDrawable == null) {
        setImageDrawable(drawable)
        return
    }

    val transitionDrawable = TransitionDrawable(arrayOf(
        currentDrawable,
        drawable
    ))
    setImageDrawable(transitionDrawable)
    transitionDrawable.startTransition(duration)
}
