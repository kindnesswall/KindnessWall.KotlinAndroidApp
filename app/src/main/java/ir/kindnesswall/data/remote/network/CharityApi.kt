package ir.kindnesswall.data.remote.network

import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.requestsmodel.RejectGiftRequestModel
import ir.kindnesswall.view.main.reviewcharity.model.CharityAndStatusModel
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

interface CharityApi {
    @GET("charity/list")
    suspend fun getCharities(): Response<List<CharityModel>>

    @GET("charity/user/{id}")
    suspend fun getCharity(@Path("id") id: Long): Response<CharityModel>

    @GET("charity/myInfo")
    suspend fun getCharityInfo(): Response<CharityAndStatusModel>

    @POST("charity/review")
    suspend fun getReviewCharity(): Response<List<CharityModel>>

    @POST("charity/review")
    suspend fun getReviewGiftsFirstPage(): Response<List<CharityModel>>

    @PUT("charity/accept/user/{id}")
    suspend fun acceptCharity(@Path("id") id: Long): Response<Any>

    @PUT("charity/reject/user/{id}")
    suspend fun rejectCharity(
        @Path("id") id: Long,
        @Body rejectGiftRequestModel: RejectGiftRequestModel
    ): Response<Any>

}