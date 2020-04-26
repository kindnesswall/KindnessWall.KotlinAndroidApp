package ir.kindnesswall.data.model

import android.os.Parcelable
import ir.kindnesswall.utils.extentions.getHourAndMinute
import kotlinx.android.parcel.Parcelize
import java.util.*

open class TextMessageBaseModel

class ChatMessageModel {
    var chat: ChatModel? = null
    var textMessages: List<TextMessageModel>? = null
}

data class TextMessageHeaderModel(var date: String = "") : TextMessageBaseModel()

@Parcelize
class TextMessageModel : TextMessageBaseModel(), Parcelable {
    var text: String? = null
    var chatId = 0
    var receiverId: Long = 0
    var id: Long = 0
    var senderId: Long = 0
    var ack = false
    var createdAt: Date? = null

    fun getTime() = createdAt.getHourAndMinute()
}