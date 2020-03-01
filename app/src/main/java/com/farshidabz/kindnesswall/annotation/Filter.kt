package com.farshidabz.kindnesswall.annotation

import androidx.annotation.StringDef
import com.farshidabz.kindnesswall.annotation.Filter.Companion.DONATED
import com.farshidabz.kindnesswall.annotation.Filter.Companion.RECEIVED
import com.farshidabz.kindnesswall.annotation.Filter.Companion.REGISTERED

@Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@StringDef(RECEIVED, DONATED, REGISTERED)
@Retention(AnnotationRetention.RUNTIME)
annotation class Filter {
    companion object {
        const val RECEIVED = "RECEIVED"
        const val DONATED = "DONATED"
        const val REGISTERED = "REGISTERED"
    }
}