package com.farshidabz.kindnesswall.utils.extentions

import android.content.Context
import android.util.DisplayMetrics


/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */


/**
 * This method converts dp unit to equivalent pixels, depending on device density.
 *
 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent px equivalent to dp depending on device density
 */
fun Int.dp(context: Context?): Int {
    var result = this
    context?.let {
        result =((this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt())
    }
    return result
}


/**
 * This method converts sp unit to equivalent pixels, depending on device density.
 *
 * @param sp A value in sp (density independent pixels) unit. Which we need to convert into pixels
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent px equivalent to sp depending on device density
 */
fun Int.sp(context: Context?): Int {
    var result = this
    context?.let {
        result =((this * (context.resources.displayMetrics.scaledDensity.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt())
    }
    return result
}


/**
 * This method converts device specific pixels to density independent pixels.
 *
 * @param px A value in px (pixels) unit. Which we need to convert into db
 * @param context Context to get resources and device specific display metrics
 * @return A float value to represent dp equivalent to px value
 */
fun Int.px(context: Context?): Int {
    var result = this
    context?.let {
    result =(this / ( it.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt() }
    return result
}
