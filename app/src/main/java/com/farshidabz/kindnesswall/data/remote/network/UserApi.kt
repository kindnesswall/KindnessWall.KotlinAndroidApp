package com.farshidabz.kindnesswall.data.remote.network

import com.farshidabz.kindnesswall.data.model.requestsmodel.UpdateProfileRequestModel
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
    fun updateUserProfile(@Body updateProfileRequestModel: UpdateProfileRequestModel): Response<Any>

    @GET("profile/{userId}")
    fun getOtherUserProfile(@Path("userId") userId: Long): Response<User>
}