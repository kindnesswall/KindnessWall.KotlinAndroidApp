package ir.kindnesswall.view.profile.blocklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kindnesswall.data.model.ChatContactModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.repository.ChatRepo
import ir.kindnesswall.data.repositories.user.UserDataSource
import javax.inject.Inject

class BlockListViewModel @Inject constructor(
    private val userRepo: UserDataSource,
    private val chatRepo: ChatRepo
) : ViewModel() {

    var blockedUsers = arrayListOf<ChatContactModel>()

    fun getBlockList(): LiveData<CustomResult<List<ChatContactModel>>> {
        return chatRepo.getBlockedUsers(viewModelScope)
    }

    fun unblockUser(chatId: Long) = chatRepo.unblockChat(viewModelScope, chatId)
}