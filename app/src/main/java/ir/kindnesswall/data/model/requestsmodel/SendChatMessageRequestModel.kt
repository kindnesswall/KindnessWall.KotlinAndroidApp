package ir.kindnesswall.data.model.requestsmodel

data class SendChatMessageRequestModel(
    var chatId: Long,
    var text: String,
    var type: String? = null
)