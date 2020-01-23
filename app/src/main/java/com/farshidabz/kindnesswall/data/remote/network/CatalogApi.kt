package com.farshidabz.kindnesswall.data.remote.network

import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.requestsmodel.GetGiftsRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


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

interface CatalogApi {
    @POST("gifts")
    suspend fun getGifts(@Body getGiftsRequestBody: GetGiftsRequestBody): Response<List<GiftModel>>

    @POST("gifts")
    suspend fun getGiftsFirstPage(): Response<List<GiftModel>>
}