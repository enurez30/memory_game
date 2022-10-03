package com.sera.memorygame.ui.flag_quiz

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.widget.ImageView
import com.sera.memorygame.utils.screenHeight
import com.sera.memorygame.utils.screenWidth

val Bitmap.toFlagDimentions: List<Int>
    get() {
        val maxWidth = screenWidth * 0.75
        val maxHeight = (screenHeight * 0.25).toInt()
        val ratio = this.width.toDouble() / this.height.toDouble()
        val newWidth = if ((maxHeight * ratio) <= maxWidth) {
            (maxHeight * ratio).toInt()
        } else {
            maxWidth.toInt()
        }
        return listOf(maxHeight, newWidth)
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
