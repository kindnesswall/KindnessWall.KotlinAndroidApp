package ir.kindnesswall.data.model

import ir.kindnesswall.data.model.user.User

data class ChatContactModel(
    var blockStatus: BlockStatus? = null,
    var chat: ChatModel? = null,
    var contactProfile: User? = null,
    var notificationCount: Int = 0
) {
    fun getUnreadMessagesCount(): String =
        if (notificationCount > 99) "99" else notificationCount.toString()
}

class BlockStatus {
    var userIsBlocked = false
    var contactIsBlocked = false
}