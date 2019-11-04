package com.farshidabz.kindnesswall.di.network

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
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

class ErrorHandlingCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) is RetrofitCall<*>) {
            return null
        }

        if (returnType !is ParameterizedType) {
            throw IllegalStateException(
                "RetrofitCall must have generic type (e.g., RetrofitCall<ResponseBody>)"
            )
        }

        val responseType = getParameterUpperBound(0, returnType)
        val callbackExecutor = retrofit.callbackExecutor()
        return ErrorHandlingCallAdapter<Any>(
            responseType,
            callbackExecutor!!
        )
    }

    private class ErrorHandlingCallAdapter<R> internal constructor(
        private val responseType: Type,
        private val callbackExecutor: Executor
    ) : CallAdapter<R, RetrofitCall<R>> {

        override fun responseType(): Type {
            return responseType
        }

        override fun adapt(call: Call<R>): RetrofitCall<R> {
            return RetrofitCallAdapter(call, callbackExecutor)
        }
    }
}
