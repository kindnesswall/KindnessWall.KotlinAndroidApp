package com.farshidabz.kindnesswall.data.remote.network

import com.farshidabz.kindnesswall.data.model.gift.GiftResponseModel
import retrofit2.Response
import retrofit2.http.GET


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
    @GET("remoteconfig")
    suspend fun getGifts(): Response<GiftResponseModel>
}