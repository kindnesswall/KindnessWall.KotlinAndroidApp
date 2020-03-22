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
    }
}