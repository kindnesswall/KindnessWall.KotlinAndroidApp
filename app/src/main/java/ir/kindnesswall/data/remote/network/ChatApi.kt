package ir.kindnesswall.data.remote.network

import ir.kindnesswall.data.model.ChatMessageModel
import ir.kindnesswall.data.model.ConversationModel
import ir.kindnesswall.data.model.TextMessageModel
import ir.kindnesswall.data.model.requestsmodel.ChatMessageAckRequestModel
import ir.kindnesswall.data.model.requestsmodel.GetChatsRequestModel
import ir.kindnesswall.data.model.requestsmodel.SendChatMessageRequestModel
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage: a interface to define end points
 *
 * How to call: just add in appInjector and repositoryImpl that you wanna use
 *
 */

interface ChatApi {
    @GET("chat/contacts")
    suspend fun getConversations(): Response<List<ConversationModel>>

    @POST("chat/messages")
    suspend fun getChats(@Body chatsRequestModel: GetChatsRequestModel): Response<ChatMessageModel>

    @POST("chat/send")
    suspend fun sendMessage(@Body sendChatMessageRequestModel: SendChatMessageRequestModel): Response<TextMessageModel>

    @PUT("chat/block/{chatId}")
    suspend fun blockChat(@Path("chatId") id: Long): Response<Any>

    @PUT("chat/unblock/{chatId}")
    suspend fun unblockChat(@Path("chatId") id: Long): Response<Any>

    @POST("chat/ack")
    suspend fun sendActMessage(@Body chatMessageAckRequestModel: ChatMessageAckRequestModel): Response<Any>
}