package ir.kindnesswall.data.model

import ir.kindnesswall.data.model.user.User
import java.io.Serializable

data class ChatContactModel(
    var blockStatus: BlockStatus,
    var chat: ChatModel? = null,
    var contactProfile: User? = null,
    var notificationCount: Int = 0
) : Serializable {
    fun getUnreadMessagesCount(): String =
        if (notificationCount > 99) "99" else notificationCount.toString()
}

class BlockStatus : Serializable {
    var userIsBlocked = false
    var contactIsBlocked = false
}