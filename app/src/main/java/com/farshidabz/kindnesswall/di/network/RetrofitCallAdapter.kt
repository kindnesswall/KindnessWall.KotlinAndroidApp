package com.farshidabz.kindnesswall.di.network

import com.farshidabz.kindnesswall.annotation.NetworkResponseType.Companion.CLIENTERROR
import com.farshidabz.kindnesswall.annotation.NetworkResponseType.Companion.DATAERROR
import com.farshidabz.kindnesswall.annotation.NetworkResponseType.Companion.NETWORKERROR
import com.farshidabz.kindnesswall.annotation.NetworkResponseType.Companion.SERVERERROR
import com.farshidabz.kindnesswall.annotation.NetworkResponseType.Companion.UNAUTHENTICATED
import com.farshidabz.kindnesswall.annotation.NetworkResponseType.Companion.UNAUTHORIZED
import com.farshidabz.kindnesswall.annotation.NetworkResponseType.Companion.UNEXPECTEDERROR
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor


/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */
/** Adapts a [Call] to [RetrofitCall].  */
internal class RetrofitCallAdapter<T>(
    private val call: Call<T>,
    private val callbackExecutor: Executor
) : RetrofitCall<T> {
    override fun cancel() {
        call.cancel()
    }

    override fun enqueue(callback: RetrofitCallback<T>) {
        call.enqueue(object : retrofit2.Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callbackExecutor.execute {
                    when (response.code()) {
                        in 200..299 -> callback.success(response)
                        401 -> callback.failure(response, networkResponseType = UNAUTHENTICATED)
                        403 -> callback.failure(response, networkResponseType = UNAUTHORIZED)
                        422 -> callback.failure(response, networkResponseType = DATAERROR)
                        in 400..499 -> callback.failure(response, networkResponseType = CLIENTERROR)
                        in 500..599 -> callback.failure(response, networkResponseType = SERVERERROR)
                        else -> callback.failure(
                            t = RuntimeException("Unexpected response $response"),
                            networkResponseType = UNEXPECTEDERROR
                        )
                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                if (t is IOException) {
                    callbackExecutor.execute {
                        callback.failure(
                            e = t,
                            networkResponseType = NETWORKERROR
                        )
                    }
                } else {
                    callbackExecutor.execute {
                        callback.failure(
                            t = t,
                            networkResponseType = UNEXPECTEDERROR
                        )
                    }
                }
            }
        })
    }
}