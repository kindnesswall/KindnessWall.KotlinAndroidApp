package ir.kindnesswall.data.remote.network

import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.local.dao.submitrequest.RegisterGiftRequestModel
import ir.kindnesswall.data.model.RequestGiftModel
import ir.kindnesswall.data.model.requestsmodel.GetGiftsRequestBaseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


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

interface GiftApi {
    @POST("gifts")
    suspend fun getGifts(@Body getGiftsRequestBody: GetGiftsRequestBaseBody): Response<List<GiftModel>>

    @POST("gifts")
    suspend fun getGiftsFirstPage(@Body getGiftsRequestBody: GetGiftsRequestBaseBody): Response<List<GiftModel>>

    @POST("gifts/register")
    suspend fun registerGift(@Body registerGiftRequestModel: RegisterGiftRequestModel): Response<GiftModel>

    @GET("gifts/request/{id}")
    suspend fun requestGift(@Path("id") id: Long): Response<RequestGiftModel>
}