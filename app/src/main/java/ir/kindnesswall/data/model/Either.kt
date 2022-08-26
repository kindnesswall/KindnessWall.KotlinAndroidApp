package ir.kindnesswall.data.model

sealed class Either<out A, out B> {
    data class Left<out A> constructor(val left: A) : Either<A, Nothing>()
    data class Right<out B> constructor(val right: B?) : Either<Nothing, B>()
}