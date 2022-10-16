package ir.kindnesswall.data.remote.network

import ir.kindnesswall.data.db.dao.catalog.GiftModel
import ir.kindnesswall.data.model.UploadImageResponse
import ir.kindnesswall.data.model.requestsmodel.GetGiftsRequestBaseBody
import ir.kindnesswall.data.model.requestsmodel.PushRegisterRequestModel
import ir.kindnesswall.data.model.requestsmodel.SettingRequestBody
import ir.kindnesswall.data.model.requestsmodel.UpdateProfileRequestBaseModel
import ir.kindnesswall.data.model.user.User
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage:`
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

interface UserApi {

    @GET("profile/{userId}")
    suspend fun getUserProfile(@Path("userId") userId: Long): Response<User>

    @POST("profile")
    suspend fun updateUserProfile(@Body updateProfileRequestModel: UpdateProfileRequestBaseModel): Response<Any>

    @POST("gifts/userReceived/{userId}")
    suspend fun getUserReceivedGifts(
        @Path("userId") userId: Long,
        @Body getGiftsRequestBaseBody: GetGiftsRequestBaseBody =
            GetGiftsRequestBaseBody().apply {
                count = Int.MAX_VALUE
            }
    ): Response<List<GiftModel>>

    @POST("gifts/userDonated/{userId}")
    suspend fun getUserDonatedGifts(
        @Path("userId") userId: Long,
        @Body getGiftsRequestBaseBody: GetGiftsRequestBaseBody =
            GetGiftsRequestBaseBody().apply {
                count = Int.MAX_VALUE
            }
    ): Response<List<GiftModel>>

    @POST("gifts/accept/{giftId}")
    suspend fun getUserAcceptedGifts(
        @Path("giftId") userId: Long,
        @Body getGiftsRequestBaseBody: GetGiftsRequestBaseBody =
            GetGiftsRequestBaseBody().apply {
                count = Int.MAX_VALUE
            }
    ): Response<List<GiftModel>>

    @POST("gifts/userDonated/{giftId}")
    suspend fun getUserRejectedGifts(
        @Path("giftId") userId: Long,
        @Body getGiftsRequestBaseBody: GetGiftsRequestBaseBody =
            GetGiftsRequestBaseBody().apply {
                count = Int.MAX_VALUE
            }
    ): Response<List<GiftModel>>

    @POST("gifts/userRegistered/{userId}")
    suspend fun getUserRegisteredGifts(
        @Path("userId") userId: Long,
        @Body getGiftsRequestBaseBody: GetGiftsRequestBaseBody =
            GetGiftsRequestBaseBody().apply {
                count = Int.MAX_VALUE
            }
    ): Response<List<GiftModel>>

    @GET("profile/{userId}")
    suspend fun getOtherUserProfile(@Path("userId") userId: Long): Response<User>

    @POST("push/register")
    fun registerFirebaseToken(@Body pushRegisterRequestModel: PushRegisterRequestModel): Call<Any>

    @POST("phone/visibility/setting")
    fun setPhoneSetting(
        settingdata: String,
        @Body settingRequest: SettingRequestBody =
            SettingRequestBody().apply {
                setting = settingdata
            }
    ): Response<Any>

    @POST("image/upload")
    suspend fun uploadImage(@Body request: RequestBody): Response<UploadImageResponse>
}