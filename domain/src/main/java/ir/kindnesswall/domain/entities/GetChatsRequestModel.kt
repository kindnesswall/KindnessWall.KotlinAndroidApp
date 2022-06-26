package ir.kindnesswall.domain.entities

data class GetChatsRequestModel(
  var chatId: Long = 1,
  var beforeId: Long = Long.MAX_VALUE,
  var count: Int = 50
)
