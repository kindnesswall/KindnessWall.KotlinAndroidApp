package ir.kindnesswall.data.repositories.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import ir.kindnesswall.data.api.ChatApi
import ir.kindnesswall.data.repositories.base.BaseDataSource
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.domain.entities.ChatContactModel
import ir.kindnesswall.domain.entities.ChatMessageModel
import ir.kindnesswall.domain.entities.TextMessageModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect

class ChatRemoteDataSourceImpl(
  private val chatApi: ChatApi
) :ChatRemoteDataSource, BaseDataSource() {

  override fun getConversationList(viewModelScope: CoroutineScope): LiveData<CustomResult<List<ChatContactModel>>> {
    TODO("Not yet implemented")
  }

  override fun getConversationList(): List<ChatContactModel>? {
    TODO("Not yet implemented")
  }


  //=
//    liveData<CustomResult<List<ChatContactModel>>>(
//      viewModelScope.coroutineContext,
//      timeoutInMs = 0
//    ) {
//      emit(CustomResult.loading())
//      getResultWithExponentialBackoffStrategy { chatApi.getConversations() }.collect { result ->
//        when (result.status) {
//          CustomResult.Status.SUCCESS -> {
//            if (result.data == null) {
//              emit(CustomResult.error(result.errorMessage))
//            } else {
//              KindnessApplication.instance.setContactList(result.data)
//              emitSource(MutableLiveData<List<ChatContactModel>>().apply {
//                value = result.data
//              }.map { CustomResult.success(it) })
//            }
//          }
//          CustomResult.Status.LOADING -> emit(CustomResult.loading())
//          else -> emit(CustomResult.error(result.errorMessage))
//        }
//      }
//    }
//
//  override suspend fun getConversationList(): List<ChatContactModel>? {
//    TODO("Not yet implemented")
//  }

  override fun getChats(
    viewModelScope: CoroutineScope,
    lastId: Long,
    chatId: Long
  ): LiveData<CustomResult<ChatMessageModel>> {
    TODO("Not yet implemented")
  }

  override fun getChatsFirstPage(
    viewModelScope: CoroutineScope,
    chatId: Long
  ): LiveData<CustomResult<ChatMessageModel>> {
    TODO("Not yet implemented")
  }

  override fun sendMessage(
    viewModelScope: CoroutineScope,
    chatId: Long,
    message: String,
    type: String?
  ): LiveData<CustomResult<TextMessageModel>> {
    TODO("Not yet implemented")
  }

  override fun blockChat(viewModelScope: CoroutineScope, chatId: Long): LiveData<CustomResult<Any?>> {
    TODO("Not yet implemented")
  }

  override fun unblockChat(viewModelScope: CoroutineScope, chatId: Long): LiveData<CustomResult<Any?>> {
    TODO("Not yet implemented")
  }

  override fun getBlockedUsers(viewModelScope: CoroutineScope): LiveData<CustomResult<List<ChatContactModel>>> {
    TODO("Not yet implemented")
  }

  override fun sendActMessage(viewModelScope: CoroutineScope, id: Long) {
    TODO("Not yet implemented")
  }

  override fun getChatId(
    viewModelScope: CoroutineScope,
    charityId: Long
  ): LiveData<CustomResult<ChatContactModel>> =
    liveData<CustomResult<ChatContactModel>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
      emit(CustomResult.loading())
      getResultWithExponentialBackoffStrategy {
        chatApi.getChatId(charityId)
      }.collect { result ->
        when (result.status) {
          CustomResult.Status.SUCCESS -> {
            if (result.data == null) {
              emit(CustomResult.error(result.errorMessage))
            } else {
              emitSource(MutableLiveData<ChatContactModel>().apply {
                value = result.data
              }.map { CustomResult.success(it) })
            }
          }
          CustomResult.Status.LOADING -> emit(CustomResult.loading())
          else -> emit(CustomResult.error(result.errorMessage))
        }
      }
    }

}