package ir.kindnesswall.view.main.conversation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.KindnessApplication
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.*
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
    var isContactListFetched: Boolean = false
    var isStartFromNotification: Boolean = false
    var chatContactModel: ChatContactModel? = null
    var isCharity: Boolean = false

    var triedToFetchCharityAndUserProfile = false

    var receiverUserId: Long = 0
    var chatId: Long = 0

    var chatList: ArrayList<TextMessageBaseModel>? = arrayListOf()

    var messageTextLiveData = MutableLiveData<String>()

    var toDonateList = arrayListOf<GiftModel>()

    var lastId = 0L

    var requestChatModel: RequestChatModel? = null

    fun getChats() = if (chatList.isNullOrEmpty()) {
        chatRepo.getChatsFirstPage(viewModelScope, chatId)
    } else {
        chatRepo.getChats(viewModelScope, lastId, chatId)
    }

    fun onMessageTextChanged(text: CharSequence) {
        messageTextLiveData.value = text.toString()
    }

    fun sendMessage(): LiveData<CustomResult<TextMessageModel>> {
        return chatRepo.sendMessage(viewModelScope, chatId, messageTextLiveData.value!!)
    }

    fun blockUser() = chatRepo.blockChat(viewModelScope, chatId)
    fun unblockUser() = chatRepo.unblockChat(viewModelScope, chatId)

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
        return userRepo.getUserProfile(viewModelScope, receiverUserId)
    }

    fun getCharityProfile(): LiveData<CustomResult<CharityModel>> {
        return charityRepo.getCharity(viewModelScope, receiverUserId)
    }

    fun getToDonateGifts(): LiveData<CustomResult<List<GiftModel>>> {
        return giftRepo.getToDonateGifts(viewModelScope, receiverUserId)
    }

    fun setSessionId() {
        requestChatModel?.let {
            AppPref.currentChatSessionId = it.chatId
            receiverUserId = it.contactId
            chatId = it.chatId
            return
        }

        chatContactModel?.let {
            AppPref.currentChatSessionId = it.chat?.chatId ?: -1
            receiverUserId = it.chat?.contactId ?: -1
            chatId = it.chat?.chatId ?: -1
        }
    }

    fun gatherLastId() {
        if (chatList == null) {
            lastId = 0
            return
        }

        val lastItem = chatList!!.last()
        if (lastItem is TextMessageModel) {
            lastId = lastItem.id
        } else {
            for (i in chatList!!.lastIndex downTo 0) {
                val item = chatList!![i]
                if (item is TextMessageModel) {
                    lastId = item.id
                }
            }
        }
    }

    fun getConversationsList() = chatRepo.getConversationList(viewModelScope)
}