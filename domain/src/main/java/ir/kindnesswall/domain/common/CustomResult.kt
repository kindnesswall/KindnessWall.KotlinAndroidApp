package ir.kindnesswall.domain.common

data class CustomResult<out T>(
  val status: Status,
  val data: T?,
  val errorMessage: ErrorMessage?,
  val serverError: Boolean = false
) {

  enum class Status {
    SUCCESS,
    ERROR,
    LOADING
  }

  data class ErrorMessage(
    var message: String? = null,
    var code: Int? = -1,
    var errorBody: String? = null
  )

  companion object {
    fun <T> success(data: T): CustomResult<T> {
      return CustomResult(Status.SUCCESS, data, null)
    }

    fun <T> error(errorMessage: ErrorMessage?, data: T? = null, serverError: Boolean = false):
      CustomResult<T> {
      return CustomResult(Status.ERROR, data, errorMessage, serverError)
    }

    fun <T> loading(data: T? = null): CustomResult<T> {
      return CustomResult(Status.LOADING, data, null)
    }
  }
}
