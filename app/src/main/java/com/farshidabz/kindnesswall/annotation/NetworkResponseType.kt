package com.farshidabz.kindnesswall.annotation


@Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class NetworkResponseType {
    companion object {
        const val NOTHING = -1
        const val SUCCESS = 200
        const val UNAUTHENTICATED = 401
        const val UNAUTHORIZED = 403
        const val DATAERROR = 422
        const val CLIENTERROR = 400
        const val SERVERERROR = 500
        const val NETWORKERROR = 700
        const val UNEXPECTEDERROR = 800
    }
}
