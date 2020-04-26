package ir.kindnesswall.annotation

import androidx.annotation.StringDef
import ir.kindnesswall.annotation.Filter.Companion.DONATED
import ir.kindnesswall.annotation.Filter.Companion.RECEIVED
import ir.kindnesswall.annotation.Filter.Companion.REGISTERED

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