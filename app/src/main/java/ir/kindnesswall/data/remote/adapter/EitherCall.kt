package ir.kindnesswall.data.remote.adapter

import ir.kindnesswall.data.model.Either
import ir.kindnesswall.data.model.Error
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

internal class EitherCall<R>(
    private val delegate: Call<R>,
    private val successType: Type,
) : Call<Either<Error, R>> {

    override fun enqueue(callback: Callback<Either<Error, R>>) {

        delegate.enqueue(object : Callback<R> {
            override fun onResponse(
                call: Call<R>,
                response: Response<R>,
            ) {
                return callback.onResponse(this@EitherCall,
                    Response.success(mapResponseToEither(response)))
            }

            override fun onFailure(call: Call<R>, throwable: Throwable) {
                callback.onResponse(
                    this@EitherCall,
                    Response.success(
                        Either.Left(
                            Error(
                                message = throwable.message ?: throwable.toString(),
                                serverError = true
                            )
                        )
                    )
                )
            }
        })
    }

    private fun <T> mapResponseToEither(
        response: Response<T>,
    ): Either<Error, T> {
        when (response.code()) {
            204 -> {
                @Suppress("UNCHECKED_CAST")
                return Either.Right(Unit) as Either<Error, T>
            }
            in 200 until 300 -> {
                val body = response.body()
                return if (body != null) Either.Right(body)
                else Either.Left(Error(message = "Response body was null"))
            }
            503 -> {
                return Either.Left(Error(message = response.message(), serverError = true))
            }
            else -> {
                return Either.Left(
                    Error(response.message(),
                        response.code(),
                        response.errorBody().toString())
                )
            }
        }
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = EitherCall(delegate.clone(), successType)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<Either<Error, R>> =
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")

    override fun request(): Request = delegate.request()
}