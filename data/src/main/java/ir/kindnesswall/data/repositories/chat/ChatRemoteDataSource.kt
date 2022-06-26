package ir.kindnesswall.data.repositories.chat

import androidx.lifecycle.LiveData
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.domain.entities.ChatContactModel
import ir.kindnesswall.domain.entities.ChatMessageModel
import ir.kindnesswall.domain.entities.TextMessageModel
import kotlinx.coroutines.CoroutineScope

interface ChatRemoteDataSource {

  suspend fun getConversationList(
    viewModelScope: CoroutineScope
  ): LiveData<CustomResult<List<ChatContactModel>>>

  suspend fun getConversationList(): List<ChatContactModel>?

  suspend fun getChats(
    viewModelScope: CoroutineScope,
    lastId: Long,
    chatId: Long
  ): LiveData<CustomResult<ChatMessageModel>>

  suspend fun getChatsFirstPage(
    viewModelScope: CoroutineScope,
    chatId: Long
  ): LiveData<CustomResult<ChatMessageModel>>

  suspend fun sendMessage(
    viewModelScope: CoroutineScope,
    chatId: Long,
    message: String,
    type: String? = null
  ): LiveData<CustomResult<TextMessageModel>>

  suspend fun blockChat(
    viewModelScope: CoroutineScope,
    chatId: Long
  ): LiveData<CustomResult<Any?>>

  suspend fun unblockChat(
    viewModelScope: CoroutineScope,
    chatId: Long
  ): LiveData<CustomResult<Any?>>

  suspend fun getBlockedUsers(
    viewModelScope: CoroutineScope
  ): LiveData<CustomResult<List<ChatContactModel>>>

  suspend fun sendActMessage(viewModelScope: CoroutineScope, id: Long)

  suspend fun getChatId(
    viewModelScope: CoroutineScope,
    charityId: Long
  ): LiveData<CustomResult<ChatContactModel>>

}