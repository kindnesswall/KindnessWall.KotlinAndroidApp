package ir.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import ir.kindnesswall.KindnessApplication
import ir.kindnesswall.data.api.ChatApi
import ir.kindnesswall.data.repositories.base.BaseDataSource
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.domain.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class ChatRepo(context: Context, private var chatApi: ChatApi) : BaseDataSource() {
    fun getConversationList(
        viewModelScope: CoroutineScope
    ): LiveData<CustomResult<List<ChatContactModel>>> =
        liveData<CustomResult<List<ChatContactModel>>>(
            viewModelScope.coroutineContext,
            timeoutInMs = 0
        ) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy { chatApi.getConversations() }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            KindnessApplication.instance.setContactList(result.data!!)
                            emitSource(MutableLiveData<List<ChatContactModel>>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    suspend fun getConversationList(): List<ChatContactModel>? {
        return try {
            val response = chatApi.getConversations()
            if (response.isSuccessful && response.body() != null) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getChats(
        viewModelScope: CoroutineScope,
        lastId: Long,
        chatId: Long
    ): LiveData<CustomResult<ChatMessageModel>> =
        liveData<CustomResult<ChatMessageModel>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                chatApi.getChats(GetChatsRequestModel(chatId, beforeId = lastId))
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<ChatMessageModel>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    fun getChatsFirstPage(
        viewModelScope: CoroutineScope,
        chatId: Long
    ): LiveData<CustomResult<ChatMessageModel>> =
        liveData<CustomResult<ChatMessageModel>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy { chatApi.getChats(GetChatsRequestModel(chatId)) }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<ChatMessageModel>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    fun sendMessage(
        viewModelScope: CoroutineScope,
        chatId: Long,
        message: String,
        type: String? = null
    ): LiveData<CustomResult<TextMessageModel>> =
        liveData<CustomResult<TextMessageModel>>(viewModelScope.coroutineContext, 0) {
            emit(CustomResult.loading())

            getResultWithExponentialBackoffStrategy {
                chatApi.sendMessage(SendChatMessageRequestModel(chatId, message, type))
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<TextMessageModel>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    fun blockChat(viewModelScope: CoroutineScope, chatId: Long): LiveData<CustomResult<Any?>> =
        liveData<CustomResult<Any?>>(viewModelScope.coroutineContext, 0) {
            emit(CustomResult.loading())

            getNullableResultWithExponentialBackoffStrategy { chatApi.blockChat(chatId) }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emit(CustomResult.success(result.data))
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    fun unblockChat(viewModelScope: CoroutineScope, chatId: Long): LiveData<CustomResult<Any?>> =
        liveData<CustomResult<Any?>>(viewModelScope.coroutineContext, 0) {
            emit(CustomResult.loading())

            getNullableResultWithExponentialBackoffStrategy { chatApi.unblockChat(chatId) }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emit(CustomResult.success(result.data))
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }

    fun getBlockedUsers(viewModelScope: CoroutineScope): LiveData<CustomResult<List<ChatContactModel>>> =
        liveData<CustomResult<List<ChatContactModel>>>(
            viewModelScope.coroutineContext,
            timeoutInMs = 0
        ) {
            getResultWithExponentialBackoffStrategy {
                chatApi.getBlockedUsers()
            }.collect { result: CustomResult<List<ChatContactModel>> ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emitSource(MutableLiveData<List<ChatContactModel>>().apply {
                            value = result.data
                        }.map { CustomResult.success(it) })
                    }

                    CustomResult.Status.ERROR -> {
                        emit(CustomResult.error(result.errorMessage))
                    }

                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                }
            }
        }

    fun sendActMessage(viewModelScope: CoroutineScope, id: Long) {
        viewModelScope.launch {
            chatApi.sendActMessage(ChatMessageAckRequestModel(id))
        }
    }

    fun getChatId(
        viewModelScope: CoroutineScope,
        charityId: Long
    ): LiveData<CustomResult<ir.kindnesswall.domain.entities.ChatContactModel>> =
        liveData<CustomResult<ir.kindnesswall.domain.entities.ChatContactModel>>(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                chatApi.getChatId(charityId)
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.errorMessage))
                        } else {
                            emitSource(MutableLiveData<ir.kindnesswall.domain.entities.ChatContactModel>().apply {
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