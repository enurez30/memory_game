package com.sera.memorygame.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
            spring.dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY

        }.start()
    }

    /**
     *
     */
    fun animateGroup(list: ArrayList<View>) {
        val set = AnimatorSet()
//        if (isFiltered) {
            var moveDuration = 500L

            set.interpolator = AnticipateOvershootInterpolator(0.8F)
            list.forEach {
                set.play(ObjectAnimator.ofFloat(it, "translationY", 2000F, 0F).setDuration(moveDuration))
                moveDuration += 100L
//                if (listener != null) {
//                    it.setOnClickListener(listener)
//                }
            }
//        } else {
//            var delay = 300L//225L
//            set.interpolator = AnticipateOvershootInterpolator(0.8F)
//            list.forEach {
//                set.play(ObjectAnimator.ofFloat(it, "translationY", 0F, 2000F).setDuration(700)).with(ObjectAnimator.ofFloat(it, "alpha", 0F).setDuration(700)).after(delay)
//                delay += -75L
//            }
//        }
//        set.addListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(p0: Animator?) {
//
//            }
//
//            override fun onAnimationEnd(p0: Animator?) {
//                callback?.onResult(true)
//            }
//
//            override fun onAnimationCancel(p0: Animator?) {
//
//            }
//
//            override fun onAnimationRepeat(p0: Animator?) {
//
//            }
//
//        })
        set.start()
    }

}