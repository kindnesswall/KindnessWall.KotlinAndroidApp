package ir.kindnesswall.view.main.conversation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.model.ConversationModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.repository.ChatRepo

class ConversationsViewModel(private val chatRepo: ChatRepo) : ViewModel() {
    val conversationsList: LiveData<CustomResult<List<ConversationModel>>> by lazy {
        chatRepo.getConversationList(viewModelScope)
    }
}