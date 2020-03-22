package com.farshidabz.kindnesswall.data.remote.network

import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.requestsmodel.UpdateProfileRequestBaseModel
import com.farshidabz.kindnesswall.data.model.user.User
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
    @POST("profile/2")
    fun getUserProfile(): Response<User>

    @POST("profile")
    fun updateUserProfile(@Body updateProfileRequestModel: UpdateProfileRequestBaseModel): Response<Any>

    @POST("gifts/userReceived/{userId}")
    fun getUserReceivedGifts(@Path("userId") userId: Long): Response<List<GiftModel>>

    @POST("gifts/userDonated/{userId}")
    fun getUserDonatedGifts(@Path("userId") userId: Long): Response<List<GiftModel>>

    @POST("gifts/userRegistered/{userId}")
    fun getUserRegisteredGifts(@Path("userId") userId: Long): Response<List<GiftModel>>

    @GET("profile/{userId}")
    fun getOtherUserProfile(@Path("userId") userId: Long): Response<User>
}