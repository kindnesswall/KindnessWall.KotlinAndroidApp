package ir.kindnesswall.data.remote.adapter

import ir.kindnesswall.data.model.Either
import ir.kindnesswall.data.model.ApiError
import ir.kindnesswall.data.model.HttpError
import ir.kindnesswall.data.model.NetworkError
import okhttp3.Request
import retrofit2.*
import java.lang.reflect.Type

internal class EitherCall<R>(
    private val delegate: Call<R>,
    private val successType: Type
) : Call<Either<ApiError, R>> {

    override fun enqueue(callback: Callback<Either<ApiError, R>>) {

        delegate.enqueue(object : Callback<R> {
            override fun onResponse(
                call: Call<R>,
                response: Response<R>
            ) {
                callback.onResponse(this@EitherCall, Response.success(response.toEither()))
            }

            private fun Response<R>.toEither(): Either<ApiError, R> {
                if (!isSuccessful) {
                    errorBody()?.let {
                        return Either.Left(
                            HttpError(message(), code(), errorBody().toString())
                        )
                    }
                }
                body()?.let { body -> return Either.Right(body) }
                return if (successType == Unit::class.java) {
                    @Suppress("UNCHECKED_CAST")
                    Either.Right(Unit) as Either<ApiError, R>
                } else {
                    @Suppress("UNCHECKED_CAST")
                    Either.Left(UnknownError("Response body was null")) as Either<ApiError, R>
                }
            }

            override fun onFailure(call: Call<R>, throwable: Throwable) {
                callback.onResponse(
                    this@EitherCall,
                    Response.success(
                        Either.Left(
                            throwable.message?.let {
                                if (it.contains("Unable to resolve host")) {
                                    NetworkError
                                } else {
                                    HttpError(message = it, serverError = true)
                                }
                            } ?: kotlin.run {
                                HttpError(message = throwable.toString(), serverError = true)
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

    override fun execute(): Response<Either<ApiError, R>> =
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")

    override fun request(): Request = delegate.request()
}