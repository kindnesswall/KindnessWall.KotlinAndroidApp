package ir.kindnesswall.data.remote.adapter

import ir.kindnesswall.data.model.Either
import ir.kindnesswall.data.model.Error
import okhttp3.Request
import retrofit2.*
import java.lang.reflect.Type

internal class EitherCall<R>(
    private val delegate: Call<R>,
    private val successType: Type
) : Call<Either<Error, R>> {

    override fun enqueue(callback: Callback<Either<Error, R>>) {

        delegate.enqueue(object : Callback<R> {
            override fun onResponse(
                call: Call<R>,
                response: Response<R>
            ) {
                callback.onResponse(this@EitherCall, Response.success(response.toEither()))
            }

            private fun Response<R>.toEither(): Either<Error, R> {
                if (!isSuccessful) {
                    errorBody()?.let {
                        return Either.Left(
                            Error(message(), code(), errorBody().toString())
                        )
                    }
                }
                body()?.let { body -> return Either.Right(body) }
                return if (successType == Unit::class.java) {
                    @Suppress("UNCHECKED_CAST")
                    Either.Right(Unit) as Either<Error, R>
                } else {
                    @Suppress("UNCHECKED_CAST")
                    Either.Left(UnknownError("Response body was null")) as Either<Error, R>
                }
            }

            override fun onFailure(call: Call<R>, throwable: Throwable) {
                callback.onResponse(
                    this@EitherCall,
                    Response.success(
                        Either.Left(
                            throwable.message?.let {
                                Error(message = it, serverError = true)
                            } ?: kotlin.run {
                                Error(message = throwable.toString(), serverError = true)
                            }
                        )
                    )
                )
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = EitherCall(delegate.clone(), successType)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<Either<Error, R>> =
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")

    override fun request(): Request = delegate.request()
}