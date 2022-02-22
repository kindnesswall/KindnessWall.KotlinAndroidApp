package ir.kindnesswall.data.remote.network

import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.dao.province.ProvinceModel
import ir.kindnesswall.data.model.*
import ir.kindnesswall.data.model.requestsmodel.RequestBaseModel
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

    @GET("application/android/version")
    suspend fun getVersion(): Response<UpdateModel>

    @POST("categories")
    suspend fun getAllCategories(@Body requestBaseModel: RequestBaseModel = RequestBaseModel()): Response<List<CategoryModel>>

    @GET("phone/visibility/setting")
    suspend fun getSetting(): Response<SettingModel>
}