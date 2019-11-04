package com.farshidabz.kindnesswall.utils.extentions

import android.graphics.Point
import android.view.View
import com.farshidabz.kindnesswall.utils.ViewAnimationUtils

fun View.hideViewWithFadeAnimation(visibility: Int = View.GONE, duration: Long = 250) {
    ViewAnimationUtils.hideViewWithFadeAnimation(this, duration.toInt(), visibility)
}

fun View.showViewWithFadeAnimation(duration: Long = 250) {
    ViewAnimationUtils.showViewWithFadeAnimation(this, duration.toInt())
}

fun View.getLocationOnScreen(): Point {
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    return Point(location[0], location[1])
}

fun View.absX(): Int {
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    return location[0]
}

fun View.absY(): Int {
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    return location[1]
}
