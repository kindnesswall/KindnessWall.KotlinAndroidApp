package ir.kindnesswall.data.remote.network

import ir.kindnesswall.data.model.hami.HamiModel
import retrofit2.Response
import retrofit2.http.GET

interface HamiApi {
    @GET("sponsors/list")
    suspend fun getHamiList(): Response<List<HamiModel>>
}