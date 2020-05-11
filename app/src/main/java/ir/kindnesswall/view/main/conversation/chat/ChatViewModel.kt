package ir.kindnesswall.view.main.conversation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.RequestGiftModel
import ir.kindnesswall.data.model.TextMessageBaseModel
import ir.kindnesswall.data.model.TextMessageModel
import ir.kindnesswall.data.model.user.User
import ir.kindnesswall.data.repository.ChatRepo
import ir.kindnesswall.data.repository.UserRepo

class ChatViewModel(private val chatRepo: ChatRepo, private val userRepo: UserRepo) : ViewModel() {
    lateinit var receiverUser: User

    var chatList: ArrayList<TextMessageBaseModel>? = arrayListOf()
    var messageTextLiveData = MutableLiveData<String>()

    var lastId = 0L

    var requestGiftModel: RequestGiftModel? = null

    fun getChats() = if (chatList.isNullOrEmpty()) {
        chatRepo.getChatsFirstPage(viewModelScope, requestGiftModel?.chatId ?: 0)
    } else {
        chatRepo.getChats(viewModelScope, lastId, requestGiftModel?.chatId ?: 0)
    }

    fun onMessageTextChanged(text: CharSequence) {
        messageTextLiveData.value = text.toString()
    }

    fun sendMessage(): LiveData<CustomResult<TextMessageModel>> {
        return chatRepo.sendMessage(
            viewModelScope,
            requestGiftModel?.chatId ?: 0,
            messageTextLiveData.value!!
        )
    }

    fun blockUser() = chatRepo.blockChat(viewModelScope, receiverUser.id)
    fun unblockUser() = chatRepo.unblockChat(viewModelScope, receiverUser.id)

    fun sendAckMessage(id: Long) = chatRepo.sendActMessage(viewModelScope, id)

    fun getUserProfile(): LiveData<CustomResult<User>> {
        return userRepo.getUserProfile(viewModelScope, requestGiftModel?.contactId ?: 0)
    }
}