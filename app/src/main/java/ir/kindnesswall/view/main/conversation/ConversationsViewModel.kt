package ir.kindnesswall.view.main.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.model.ChatContactModel
import ir.kindnesswall.data.repository.ChatRepo

class ConversationsViewModel(private val chatRepo: ChatRepo) : ViewModel() {
    fun getConversationsList() = chatRepo.getConversationList(viewModelScope)
    var conversationsList = arrayListOf<ChatContactModel>()
}