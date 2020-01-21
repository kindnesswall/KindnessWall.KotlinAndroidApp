package com.farshidabz.kindnesswall.data.model

data class CustomResult<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val serverError: Boolean = false
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): CustomResult<T> {
            return CustomResult(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String?, data: T? = null, serverError: Boolean = false): CustomResult<T> {
            return CustomResult(Status.ERROR, data, message, serverError)
        }

        fun <T> loading(data: T? = null): CustomResult<T> {
            return CustomResult(Status.LOADING, data, null)
        }
    }
}