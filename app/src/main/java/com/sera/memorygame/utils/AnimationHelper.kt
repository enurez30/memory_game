package com.sera.memorygame.utils

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

object AnimationHelper {

    /**
     *
     */
    fun animateSpringY(targetView: View, finalPosition: Float) {
        SpringAnimation(targetView, DynamicAnimation.TRANSLATION_Y, finalPosition).apply {
            spring.stiffness = SpringForce.STIFFNESS_LOW
            spring.dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY

        }.start()
    }

    /**
     *
     */
    fun animateGroup(list: ArrayList<View>) {
        val set = AnimatorSet()
        var moveDuration = 500L

        set.interpolator = AnticipateOvershootInterpolator(0.8F)
        list.forEach {
            set.play(ObjectAnimator.ofFloat(it, "translationY", 2000F, 0F).setDuration(moveDuration))
            moveDuration += 100L

        }
        set.start()
    }

    /**
     *
     */
    @SuppressLint("ObjectAnimatorBinding")
    fun animateColorBackground(target: View, fromColor: Int, toColor: Int, duration: Long) {
        val animator = ObjectAnimator.ofInt(target, "backgroundTint", fromColor, toColor)
        animator.duration = duration
        animator.setEvaluator(ArgbEvaluator())
        animator.interpolator = AnticipateOvershootInterpolator(2F)
        animator.addUpdateListener {
            val value = (it.animatedValue as Int)
            target.backgroundTintList = ColorStateList.valueOf(value)
        }
        animator.start()
    }
}