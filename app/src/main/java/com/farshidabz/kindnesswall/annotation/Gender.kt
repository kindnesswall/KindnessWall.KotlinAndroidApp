package com.farshidabz.kindnesswall.annotation

import androidx.annotation.StringDef
import com.farshidabz.kindnesswall.annotation.Gender.Companion.FEMALE
import com.farshidabz.kindnesswall.annotation.Gender.Companion.MALE

@Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@StringDef(MALE, FEMALE)
@Retention(AnnotationRetention.RUNTIME)
annotation class Gender {
    companion object {
        const val MALE = "MALE"
        const val FEMALE = "FEMALE"
    }
}