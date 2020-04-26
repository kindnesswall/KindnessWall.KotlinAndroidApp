package ir.kindnesswall.annotation

import androidx.annotation.IntDef
import ir.kindnesswall.annotation.OnBoardingLayoutType.Companion.BENEFITS
import ir.kindnesswall.annotation.OnBoardingLayoutType.Companion.CITY

@Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@IntDef(BENEFITS, CITY)
@Retention(AnnotationRetention.RUNTIME)
annotation class OnBoardingLayoutType {
    companion object {
        const val BENEFITS = 0
        const val CITY = 1
    }
}