package com.farshidabz.kindnesswall.data.remote.network

import com.farshidabz.kindnesswall.data.local.dao.province.ProvinceModel
import com.farshidabz.kindnesswall.data.model.CityModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage: a interface to define end points
 *
 * How to call: just add in appInjector and repositoryImpl that you wanna use
 *
 */

interface GeneralApi {
    @GET("provinces")
    suspend fun getProvinces(): Response<List<ProvinceModel>>

    @POST("cities/{provincesId}")
    suspend fun getCities(@Query("provincesId") id: Int): Response<List<CityModel>>
}