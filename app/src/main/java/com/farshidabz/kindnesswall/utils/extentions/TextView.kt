package com.farshidabz.kindnesswall.utils.extentions

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat


fun TextView.changeTextColor(
    fromColor: Int,
    toColor: Int,
    duration: Long = 200
) {

    val from = FloatArray(3)
    val to = FloatArray(3)

    Color.colorToHSV(fromColor, from)   // from white
    Color.colorToHSV(toColor, to)     // to red

    val colorAnimator = ValueAnimator.ofFloat(0f, 1f)   // animate from 0 to 1
    colorAnimator.duration = duration                              // for 300 ms

    val hsv = FloatArray(3)
    // transition color
    colorAnimator.addUpdateListener { valueAnimator ->
        // Transition along each axis of HSV (hue, saturation, value)
        hsv[0] = from[0] + (to[0] - from[0]) * valueAnimator.animatedFraction
        hsv[1] = from[1] + (to[1] - from[1]) * valueAnimator.animatedFraction
        hsv[2] = from[2] + (to[2] - from[2]) * valueAnimator.animatedFraction

        this.setTextColor(Color.HSVToColor(hsv))
    }
    colorAnimator.start()
}

fun TextView.changeTextColorRange(
    fromColor: Int,
    toColor: Int,
    range: Float = 1f
) {

    val from = FloatArray(3)
    val to = FloatArray(3)

    Color.colorToHSV(fromColor, from)   // from white
    Color.colorToHSV(toColor, to)     // to red

    val hsv = FloatArray(3)
    // transition color
    // Transition along each axis of HSV (hue, saturation, value)
    hsv[0] = from[0] + (to[0] - from[0]) * range
    hsv[1] = from[1] + (to[1] - from[1]) * range
    hsv[2] = from[2] + (to[2] - from[2]) * range

    this.setTextColor(Color.HSVToColor(hsv))
}


fun TextView.setDrawableTintResource(color: Int) {
    for (drawable in this.compoundDrawables) {
        if (drawable != null) {
            drawable.mutate().colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(
                    this.context,
                    color
                ), PorterDuff.Mode.SRC_IN
            )
        }
    }
}

fun TextView.setDrawableTintColor(color: Int) {
    for (drawable in this.compoundDrawables) {
        if (drawable != null) {
            drawable.mutate().colorFilter = PorterDuffColorFilter(
                color, PorterDuff.Mode.SRC_IN
            )
        }
    }
}

fun TextView.concatBold(boldText: String) {
    val spannedString = SpannableStringBuilder(text).append(boldText)
    spannedString.setSpan(
        android.text.style.StyleSpan(Typeface.BOLD),
        text.length,
        spannedString.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    text = spannedString
}

fun TextView.concatColored(coloredText: String, color: Int) {
    val spannedString = SpannableStringBuilder(text).append(coloredText)

    spannedString.setSpan(
        ForegroundColorSpan(color),
        text.length,
        spannedString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    text = spannedString
}

fun TextView.concatBoldAndColored(targetText: String, color: Int) {
    val spannedString = SpannableStringBuilder(text).append(targetText)

    spannedString.setSpan(
        android.text.style.StyleSpan(Typeface.BOLD),
        text.length,
        spannedString.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannedString.setSpan(
        ForegroundColorSpan(color),
        text.length,
        spannedString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    text = spannedString
}

fun TextView.isNormal(isNormal: Boolean) {
    when (isNormal) {
        false -> this.setTypeface(null, Typeface.BOLD)
        true -> this.setTypeface(null, Typeface.NORMAL)
    }
}