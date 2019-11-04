package com.farshidabz.kindnesswall.utils.extentions

fun Double.format(digits: Int): String = java.lang.String.format("%.${digits}f", this)