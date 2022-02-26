package ir.kindnesswall.data.remote.network

import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.CharityReportMessageModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @POST("charity/report")
    suspend fun addMessageCharity(@Body model:CharityReportMessageModel):Response<Any>
}