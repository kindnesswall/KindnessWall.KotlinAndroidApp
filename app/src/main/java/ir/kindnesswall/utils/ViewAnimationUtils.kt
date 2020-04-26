package ir.kindnesswall.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator

object ViewAnimationUtils {


    const val TRANSLATE_DIRECTION_TOP = 1
    const val TRANSLATE_DIRECTION_BOTTOM = 2
    private const val TRANSLATE_DURATION_MILLIS = 100
    private val mInterpolator = AccelerateDecelerateInterpolator()
    internal var animatedItemCount = 0

    fun hideView(view: View, translateType: Int, duration: Int = TRANSLATE_DURATION_MILLIS) {
        toggleView(
            view,
            false,
            true,
            true,
            translateType
        )
    }

    private fun toggleView(
        view: View,
        visible: Boolean,
        animate: Boolean = true,
        force: Boolean = true,
        translateType: Int,
        duration: Int = TRANSLATE_DURATION_MILLIS
    ) {

        if (force) {
            val height = view.height
            var margin = 0
            if (translateType == TRANSLATE_DIRECTION_TOP)
                margin = (if (visible) 0 else height + getMarginTop(
                    view
                )) * -1
            else if (translateType == TRANSLATE_DIRECTION_BOTTOM)
                margin = if (visible) 0 else height + getMarginBottom(
                    view
                )
            val translationY = margin
            if (animate) {
                if (animatedItemCount < 1)
                    view.animate().setInterpolator(mInterpolator)
                        .setDuration(duration.toLong())
                        .setListener(object : Animator.AnimatorListener {

                            override fun onAnimationStart(animation: Animator) {
                                animatedItemCount++
                            }

                            override fun onAnimationEnd(animation: Animator) {
                                animatedItemCount--
                            }

                            override fun onAnimationCancel(animation: Animator) {

                            }

                            override fun onAnimationRepeat(animation: Animator) {

                            }
                        })
                        .translationY(translationY.toFloat())
            } else {
                view.translationY = translationY.toFloat()
            }
        }
    }

    private fun getMarginBottom(view: View): Int {
        var marginBottom = 0
        val layoutParams = view.layoutParams
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            marginBottom = layoutParams.bottomMargin
        }
        return marginBottom
    }

    private fun getMarginTop(view: View): Int {
        var marginTop = 0
        val layoutParams = view.layoutParams
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            marginTop = layoutParams.topMargin
        }
        return marginTop
    }

    fun hideViewWithFadeAnimation(view: View, duration: Int) {
        view.alpha = 1f
        view.animate()
            .alpha(0f)
            .setDuration(duration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.animate().setListener(null)
                    view.visibility = View.GONE
                }
            })

    }

    fun hideViewWithFadeAnimation(view: View, duration: Int, hideType: Int) {
        view.clearAnimation()
        view.alpha = 1f
        view.animate()
            .alpha(0f)
            .setDuration(duration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    view.animate().setListener(null)
                    view.visibility = hideType
                }
            }).start()

    }

    fun showViewWithFadeAnimation(view: View, duration: Int) {
        view.animate().setListener(null)
        view.clearAnimation()
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .alpha(1f).duration = duration.toLong()
    }

    fun showView(view: View, translateType: Int, duration: Int = TRANSLATE_DURATION_MILLIS) {
        toggleView(
            view,
            true,
            true,
            true,
            translateType,
            duration
        )
    }

}
