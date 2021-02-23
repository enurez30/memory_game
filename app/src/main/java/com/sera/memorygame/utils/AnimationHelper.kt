package com.sera.memorygame.utils

import android.view.View
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

}