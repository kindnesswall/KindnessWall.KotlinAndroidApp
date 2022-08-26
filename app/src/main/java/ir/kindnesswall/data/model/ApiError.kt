package ir.kindnesswall.data.model

sealed class ApiError
data class HttpError(
    var message: String? = null,
    var code: Int? = -1,
    var errorBody: String? = null,
    val serverError: Boolean = false
) : ApiError()

object NetworkError : ApiError()