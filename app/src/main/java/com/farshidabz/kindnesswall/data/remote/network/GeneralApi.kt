package com.farshidabz.kindnesswall.data.remote.network

import com.farshidabz.kindnesswall.data.local.AppPref
import com.farshidabz.kindnesswall.data.local.dao.province.ProvinceModel
import com.farshidabz.kindnesswall.data.model.CategoryModel
import com.farshidabz.kindnesswall.data.model.CityModel
import com.farshidabz.kindnesswall.data.model.RegionModel
import com.farshidabz.kindnesswall.data.model.requestsmodel.RequestBaseModel
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

interface GeneralApi {
    @GET("provinces/{countryId}")
    suspend fun getProvinces(@Path("countryId") countryId: Int = AppPref.countryId): Response<List<ProvinceModel>>

    @GET("cities/{provincesId}")
    suspend fun getCities(@Path("provincesId") id: Int): Response<List<CityModel>>

    @GET("regions/{cityId}")
    suspend fun getRegions(@Path("cityId") id: Int): Response<List<RegionModel>>

    @POST("categories")
    suspend fun getAllCategories(@Body requestBaseModel: RequestBaseModel = RequestBaseModel()): Response<List<CategoryModel>>
}