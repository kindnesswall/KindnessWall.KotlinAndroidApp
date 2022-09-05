package ir.kindnesswall.data.remote.adapter

import ir.kindnesswall.data.model.Either
import ir.kindnesswall.data.model.Error
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

internal class EitherCallAdapter<R>(
    private val successType: Type,
) : CallAdapter<R, Call<Either<Error, R>>> {

    @Suppress("UNCHECKED_CAST")
    override fun adapt(
        call: Call<R>
    ): Call<Either<Error, R>> =
        EitherCall(call, successType)

    override fun responseType(): Type = successType
}