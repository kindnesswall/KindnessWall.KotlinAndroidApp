package ir.kindnesswall.data.model

data class Error(
    var message: String? = null,
    var code: Int? = -1,
    var errorBody: String? = null,
    val serverError: Boolean = false,
)