package ir.kindnesswall.view.main.conversation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.KindnessApplication
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.RequestChatModel
import ir.kindnesswall.data.model.TextMessageBaseModel
import ir.kindnesswall.data.model.TextMessageModel
import ir.kindnesswall.data.model.user.User
import ir.kindnesswall.data.repository.CharityRepo
import ir.kindnesswall.data.repository.ChatRepo
import ir.kindnesswall.data.repository.GiftRepo
import ir.kindnesswall.data.repository.UserRepo

class ChatViewModel(
    private val chatRepo: ChatRepo,
    private val userRepo: UserRepo,
    private val giftRepo: GiftRepo,
    private val charityRepo: CharityRepo
) : ViewModel() {
    var isCharity: Boolean = false

    var receiverUserId: Long = 0

    var chatList: ArrayList<TextMessageBaseModel>? = arrayListOf()
    var messageTextLiveData = MutableLiveData<String>()

    var toDonateList = arrayListOf<GiftModel>()

    var lastId = 0L

    var requestChatModel: RequestChatModel? = null

    fun getChats() = if (chatList.isNullOrEmpty()) {
        chatRepo.getChatsFirstPage(viewModelScope, requestChatModel?.chatId ?: 0)
    } else {
        chatRepo.getChats(viewModelScope, lastId, requestChatModel?.chatId ?: 0)
    }

    fun onMessageTextChanged(text: CharSequence) {
        messageTextLiveData.value = text.toString()
    }

    fun sendMessage(): LiveData<CustomResult<TextMessageModel>> {
        return chatRepo.sendMessage(
            viewModelScope,
            requestChatModel?.chatId ?: 0,
            messageTextLiveData.value!!
        )
    }

    fun blockUser() = chatRepo.blockChat(viewModelScope, requestChatModel?.chatId ?: 0)
    fun unblockUser() = chatRepo.unblockChat(viewModelScope, requestChatModel?.chatId ?: 0)

    fun sendAckMessage(messageId: Long, chatId: Long) {
        val contact = KindnessApplication.instance.getContact(chatId)

        contact?.let {
            it.notificationCount = it.notificationCount - 1
            if (it.notificationCount < 0) {
                it.notificationCount = 0
            }

            KindnessApplication.instance.updateContactList(it)
        }

        chatRepo.sendActMessage(viewModelScope, messageId)
    }

    fun getUserProfile(): LiveData<CustomResult<User>> {
        return userRepo.getUserProfile(viewModelScope, requestChatModel?.contactId ?: 0)
    }

    fun getCharityProfile(): LiveData<CustomResult<CharityModel>> {
        return charityRepo.getCharity(viewModelScope, requestChatModel?.contactId ?: 0)
    }

    fun getToDonateGifts(): LiveData<CustomResult<List<GiftModel>>> {
        return giftRepo.getToDonateGifts(viewModelScope, requestChatModel?.contactId ?: 0)
    }
}