package com.farshidabz.kindnesswall.data.repository

import android.content.Context
import android.net.Uri
import android.os.FileUtils
import com.farshidabz.kindnesswall.data.model.BaseDataSource
import com.farshidabz.kindnesswall.data.model.UploadImageResponse
import com.farshidabz.kindnesswall.data.remote.network.UploadFileApi
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FileUploadRepo(
    val context: Context,
    private val uploadFileApi: UploadFileApi
) : BaseDataSource() {
    private fun uploadFile(fileUri: Uri) {

        val file = FileUtils.getFile(this, fileUri)

        // create RequestBody instance from file
        val type = context.contentResolver.getType(fileUri) ?: return
        val requestFile = RequestBody.create(MediaType.parse(type), file)
        val body = MultipartBody.Part.createFormData("image", file.getName(), requestFile)

        uploadFileApi.uploadPhoto().enqueue(object : Callback<UploadImageResponse> {
            override fun onFailure(call: Call<UploadImageResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<UploadImageResponse>,
                response: Response<UploadImageResponse>
            ) {
            }

        })
        // finally, execute the request
//        val call = uploadFileApi.uploadPhoto(description, body)
//        call.enqueue(object : Callback<ResponseBody>() {
//            fun onResponse(
//                call: Call<ResponseBody>,
//                response: Response<ResponseBody>
//            ) {
//                Log.v("Upload", "success")
//            }
//
//            fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Log.e("Upload error:", t.message)
//            }
//        })
    }
}