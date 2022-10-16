package ir.kindnesswall.data.repositories.chat

import androidx.lifecycle.LiveData
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.domain.entities.ChatContactModel
import ir.kindnesswall.domain.entities.ChatMessageModel
import ir.kindnesswall.domain.entities.TextMessageModel
import kotlinx.coroutines.CoroutineScope

interface ChatRemoteDataSource {

  fun getConversationList(
    viewModelScope: CoroutineScope
  ): LiveData<CustomResult<List<ChatContactModel>>>

   fun getConversationList(): List<ChatContactModel>?

   fun getChats(
    viewModelScope: CoroutineScope,
    lastId: Long,
    chatId: Long
  ): LiveData<CustomResult<ChatMessageModel>>

   fun getChatsFirstPage(
    viewModelScope: CoroutineScope,
    chatId: Long
  ): LiveData<CustomResult<ChatMessageModel>>

   fun sendMessage(
    viewModelScope: CoroutineScope,
    chatId: Long,
    message: String,
    type: String? = null
  ): LiveData<CustomResult<TextMessageModel>>

   fun blockChat(
    viewModelScope: CoroutineScope,
    chatId: Long
  ): LiveData<CustomResult<Any?>>

   fun unblockChat(
    viewModelScope: CoroutineScope,
    chatId: Long
  ): LiveData<CustomResult<Any?>>

   fun getBlockedUsers(
    viewModelScope: CoroutineScope
  ): LiveData<CustomResult<List<ChatContactModel>>>

  fun sendActMessage(viewModelScope: CoroutineScope, id: Long)

  fun getChatId(
    viewModelScope: CoroutineScope,
    charityId: Long
  ): LiveData<CustomResult<ChatContactModel>>

}