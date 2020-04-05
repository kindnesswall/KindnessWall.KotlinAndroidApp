package com.farshidabz.kindnesswall.data.remote.network

import com.farshidabz.kindnesswall.data.model.UploadImageResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface UploadFileApi {
    @Multipart
    @FormUrlEncoded
    @POST("image/upload")
    fun uploadPhoto(@Part image: MultipartBody.Part): Call<UploadImageResponse>
}