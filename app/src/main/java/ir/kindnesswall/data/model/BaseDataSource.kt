package ir.kindnesswall.data.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import retrofit2.Response


abstract class BaseDataSource {

    private suspend fun <T> getResult(call: suspend () -> Response<T>): CustomResult<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return CustomResult.success(body) else {
                    if (response.message().toString().toLowerCase().contains("ok")) {
                        return CustomResult.success(body as T)
                    }
                }
            }

            return CustomResult.error(getErrorMessage(response), serverError = true)
        } catch (e: Exception) {
            return CustomResult.error(
                CustomResult.ErrorMessage(e.message ?: e.toString()),
                serverError = false
            )
        }
    }

    private fun <T> getErrorMessage(response: Response<T>): CustomResult.ErrorMessage {
        return CustomResult.ErrorMessage(
            response.message(),
            response.code(),
            response.errorBody()?.string()
        )
    }

    protected fun <T> getResultWithExponentialBackoffStrategy(
        times: Int = 2,
        initialDelay: Long = 100,
        maxDelay: Long = 10000,
        factor: Double = 2.0,
        call: suspend () -> Response<T>
    ) = flow {
        var loopTimes = times
        var currentDelay = initialDelay
        loop@ while (loopTimes - 1 != 0) {
            loopTimes--
            val response = getResult(call)
            when (response.status) {
                CustomResult.Status.SUCCESS -> {
                    emit(CustomResult.success(response.data))
                    break@loop
                }
                CustomResult.Status.ERROR -> {
                    emit(CustomResult.error(response.errorMessage, response.data, response.serverError))
                    if (response.serverError) break@loop
                }
            }

            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
    }
}
