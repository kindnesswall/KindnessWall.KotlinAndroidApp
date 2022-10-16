package ir.kindnesswall.data.model

import ir.kindnesswall.domain.entities.ChatModel
import ir.kindnesswall.utils.extentions.getHourAndMinute
import java.io.Serializable
import java.util.*

open class TextMessageBaseModel

class ChatMessageModel {
    var chat: ChatModel? = null
    var textMessages: List<TextMessageModel>? = null
}

data class TextMessageHeaderModel(var date: String = "") : TextMessageBaseModel()

data class TextMessageDonateModel(var date: String = "") : TextMessageBaseModel()

class TextMessageModel : TextMessageBaseModel(), Serializable {
    var text: String? = null
    var chatId: Long = 0
    var receiverId: Long = 0
    var id: Long = 0
    var senderId: Long = 0
    var ack = false
    var createdAt: Date? = null
    var type: String? = "default"

    fun getTime() = createdAt.getHourAndMinute()
}