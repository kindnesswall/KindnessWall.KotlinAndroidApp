package ir.kindnesswall.domain.entities

data class SendChatMessageRequestModel(
  var chatId: Long,
  var text: String,
  var type: String? = null
)
