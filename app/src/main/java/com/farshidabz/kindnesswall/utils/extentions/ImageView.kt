package com.farshidabz.kindnesswall.utils.extentions

import android.widget.ImageView

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

fun ImageView.toCircle()
{
    this.setImageBitmap(this.drawable.toBitmap().toCircle())
}