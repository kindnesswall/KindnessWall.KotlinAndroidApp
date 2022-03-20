package ir.kindnesswall.data.remote.network

import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.local.dao.submitrequest.RegisterGiftRequestModel
import ir.kindnesswall.data.model.*
import ir.kindnesswall.data.model.requestsmodel.DonateGiftRequestModel
import ir.kindnesswall.data.model.requestsmodel.GetGiftsRequestBaseBody
import ir.kindnesswall.data.model.requestsmodel.RejectGiftRequestModel
import retrofit2.Response
import retrofit2.http.*


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

    @POST("gifts/register")
    suspend fun registerGift(@Body registerGiftRequestModel: RegisterGiftRequestModel): Response<GiftModel>

    @PUT("gifts/{id}")
    suspend fun updateGift(
        @Path("id") id: Long,
        @Body registerGiftRequestModel: RegisterGiftRequestModel
    ): Response<GiftModel>

    @DELETE("gifts/{id}")
    suspend fun deleteGift(@Path("id") id: Long): Response<Any>

    @GET("phone/visibility/setting/{userId}")
    suspend fun getSetting(@Path("userId") userId: Long): Response<SettingModel>

    @GET("phone/visibility/access/{userId}")
    suspend fun getUserNumber(@Path("userId") userId: Long): Response<PhoneNumberModel>

    @POST("gifts/todonate/{userId}")
    suspend fun getToDonateGifts(
        @Path("userId") userId: Long,
        @Body getGiftsRequestBody: GetGiftsRequestBaseBody
    ): Response<List<GiftModel>>

    @POST("donate")
    suspend fun donateGift(@Body donateGiftRequestModel: DonateGiftRequestModel): Response<Any>

    @GET("gifts/request/{id}")
    suspend fun requestGift(@Path("id") id: Long): Response<ChatContactModel>

    @POST("gifts/review")
    suspend fun getReviewGifts(@Body getGiftsRequestBody: GetGiftsRequestBaseBody): Response<List<GiftModel>>

    @POST("gifts/review")
    suspend fun getReviewGiftsFirstPage(@Body getGiftsRequestBody: GetGiftsRequestBaseBody): Response<List<GiftModel>>

    @PUT("gifts/reject/{id}")
    suspend fun rejectGift(
        @Path("id") id: Long,
        @Body rejectGiftRequestModel: RejectGiftRequestModel
    ): Response<Any>

    @PUT("gifts/accept/{id}")
    suspend fun acceptGift(@Path("id") id: Long): Response<Any>

    @GET("gifts/request/status/{id}")
    suspend fun getGiftRequestStatus(@Path("id") id: Long): Response<GiftRequestStatusModel>

    @POST("phone/visibility/setting")
    suspend fun setPhoneVisibilitySetting(@Body setSetting: SetSetting): Response<Any>

    @GET("phone/visibility/setting")
    suspend fun getPhoneVisibilitySetting(): Response<SettingModel>

    @POST("gifts/report")
    suspend fun sendReport(@Body model: ReportGiftMessageModel): Response<Any>
}