package ir.kindnesswall.view.main.conversation.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.model.TextMessageBaseModel
import ir.kindnesswall.data.repository.ChatRepo

class ChatViewModel(private val chatRepo: ChatRepo) : ViewModel() {
    var chatList: ArrayList<TextMessageBaseModel>? = arrayListOf()
    var messageTextLiveData = MutableLiveData<String>()

    var lastId = 0L
    var chatId = 0L

    fun getChats() = /*if (chatList.isNullOrEmpty()) {*/
        chatRepo.getChatsFirstPage(viewModelScope, chatId)
//    } else {
//        chatRepo.getChats(viewModelScope, lastId, chatId)
//    }

    fun onMessageTextChanged(text: CharSequence) {
        messageTextLiveData.value = text.toString()
    }

    fun sendMessage() = chatRepo.sendMessage(viewModelScope, chatId, messageTextLiveData.value!!)
    fun blockUser() = chatRepo.blockChat(viewModelScope, chatId)
    fun unblockUser() = chatRepo.unblockChat(viewModelScope, chatId)

    fun sendAckMessage(id: Long) = chatRepo.sendActMessage(viewModelScope, id)
}