package ir.kindnesswall.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import ir.kindnesswall.data.model.*
import ir.kindnesswall.data.model.requestsmodel.ChatMessageAckRequestModel
import ir.kindnesswall.data.model.requestsmodel.GetChatsRequestModel
import ir.kindnesswall.data.model.requestsmodel.SendChatMessageRequestModel
import ir.kindnesswall.data.remote.network.ChatApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect


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

class ChatRepo(val context: Context, private var chatApi: ChatApi) : BaseDataSource() {
    fun getConversationList(
        viewModelScope: CoroutineScope
    ): LiveData<CustomResult<List<ConversationModel>>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy { chatApi.getConversations() }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.message.toString()))
                        } else {
                            emitSource(MutableLiveData<List<ConversationModel>>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.message.toString()))
                }
            }
        }

    fun getChats(
        viewModelScope: CoroutineScope,
        lastId: Long,
        chatId: Long
    ): LiveData<CustomResult<ChatMessageModel>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy {
                chatApi.getChats(GetChatsRequestModel(chatId, beforeId = lastId))
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.message.toString()))
                        } else {
                            emitSource(MutableLiveData<ChatMessageModel>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.message.toString()))
                }
            }
        }

    fun getChatsFirstPage(
        viewModelScope: CoroutineScope,
        chatId: Long
    ): LiveData<CustomResult<ChatMessageModel>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            emit(CustomResult.loading())
            getResultWithExponentialBackoffStrategy { chatApi.getChats(GetChatsRequestModel(chatId)) }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        if (result.data == null) {
                            emit(CustomResult.error(result.message.toString()))
                        } else {
                            emitSource(MutableLiveData<ChatMessageModel>().apply {
                                value = result.data
                            }.map { CustomResult.success(it) })
                        }
                    }
                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                    else -> emit(CustomResult.error(result.message.toString()))
                }
            }
        }

    fun sendMessage(
        viewModelScope: CoroutineScope,
        chatId: Long,
        message: String
    ): LiveData<CustomResult<TextMessageModel>> = liveData(viewModelScope.coroutineContext, 0) {
        emit(CustomResult.loading())

        getResultWithExponentialBackoffStrategy {
            chatApi.sendMessage(
                SendChatMessageRequestModel(chatId, message)
            )
        }.collect { result ->
            when (result.status) {
                CustomResult.Status.SUCCESS -> {
                    if (result.data == null) {
                        emit(CustomResult.error(result.message.toString()))
                    } else {
                        emitSource(MutableLiveData<TextMessageModel>().apply {
                            value = result.data
                        }.map { CustomResult.success(it) })
                    }
                }
                CustomResult.Status.LOADING -> emit(CustomResult.loading())
                else -> emit(CustomResult.error(result.message.toString()))
            }
        }
    }

    fun blockChat(
        viewModelScope: CoroutineScope,
        chatId: Long
    ): LiveData<CustomResult<Any>> = liveData(viewModelScope.coroutineContext, 0) {
        emit(CustomResult.loading())

        getResultWithExponentialBackoffStrategy { chatApi.blockChat(chatId) }.collect { result ->
            when (result.status) {
                CustomResult.Status.SUCCESS -> {
                    if (result.data == null) {
                        emit(CustomResult.error(result.message.toString()))
                    } else {
                        emit(CustomResult.success(result.data))
                    }
                }
                CustomResult.Status.LOADING -> emit(CustomResult.loading())
                else -> emit(CustomResult.error(result.message.toString()))
            }
        }
    }

    fun unblockChat(
        viewModelScope: CoroutineScope,
        chatId: Long
    ): LiveData<CustomResult<Any>> = liveData(viewModelScope.coroutineContext, 0) {
        emit(CustomResult.loading())

        getResultWithExponentialBackoffStrategy { chatApi.unblockChat(chatId) }.collect { result ->
            when (result.status) {
                CustomResult.Status.SUCCESS -> {
                    if (result.data == null) {
                        emit(CustomResult.error(result.message.toString()))
                    } else {
                        emit(CustomResult.success(result.data))
                    }
                }
                CustomResult.Status.LOADING -> emit(CustomResult.loading())
                else -> emit(CustomResult.error(result.message.toString()))
            }
        }
    }

    fun sendActMessage(viewModelScope: CoroutineScope, id: Long): LiveData<CustomResult<Boolean>> =
        liveData(viewModelScope.coroutineContext, timeoutInMs = 0) {
            getResultWithExponentialBackoffStrategy {
                chatApi.sendActMessage(ChatMessageAckRequestModel(id))
            }.collect { result ->
                when (result.status) {
                    CustomResult.Status.SUCCESS -> {
                        emitSource(MutableLiveData<Boolean>().apply { value = true }
                            .map { CustomResult.success(it) })
                    }

                    CustomResult.Status.ERROR -> {
                        emit(CustomResult.error(""))
                    }

                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
                }
            }
        }
}