package com.farshidabz.kindnesswall.view.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.farshidabz.kindnesswall.BuildConfig
import com.farshidabz.kindnesswall.annotation.Filter
import com.farshidabz.kindnesswall.data.local.UserInfoPref
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.UploadImageResponse
import com.farshidabz.kindnesswall.data.repository.UserRepo
import com.farshidabz.kindnesswall.utils.wrapInBearer
import com.google.gson.Gson
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import java.io.File

class MyProfileViewModel(private val userRepo: UserRepo) : ViewModel() {
    val newUserName: String = ""
    var newImageUrlLiveData =
        MutableLiveData<UploadImageResponse>().apply { value = UploadImageResponse("") }

    var selectedImageFile: File? = null
    lateinit var selectedImagePath: String

    var currentFilter = Filter.REGISTERED

    fun getGifts(): LiveData<CustomResult<List<GiftModel>>> {
        return when (currentFilter) {
            Filter.REGISTERED -> userRepo.getUserRegisteredGifts(
                viewModelScope,
                UserInfoPref.userId
            )

            Filter.DONATED -> userRepo.getUserDonatedGifts(viewModelScope, UserInfoPref.userId)
            Filter.RECEIVED -> userRepo.getUserReceivedGifts(viewModelScope, UserInfoPref.userId)
            else -> userRepo.getUserReceivedGifts(viewModelScope, UserInfoPref.userId)
        }
    }

    fun uploadImage(context: Context, lifecycleOwner: LifecycleOwner) {
        val request: MultipartUploadRequest =
            MultipartUploadRequest(context, serverUrl = "${BuildConfig.URL_WEBAPI}/image/upload")
                .setMethod("POST")
                .addFileToUpload(filePath = selectedImagePath, parameterName = "image")

        request.addHeader("Authorization", wrapInBearer(UserInfoPref.bearerToken))

        request.subscribe(context, lifecycleOwner, object : RequestObserverDelegate {
            override fun onCompleted(context: Context, uploadInfo: UploadInfo) {
                Log.e("LIFECYCLE", "Completed ")
            }

            override fun onCompletedWhileNotObserving() {
            }

            override fun onError(context: Context, uploadInfo: UploadInfo, exception: Throwable) {
                Log.e("LIFECYCLE", "Error " + exception.message)
            }

            override fun onProgress(context: Context, uploadInfo: UploadInfo) {
                Log.e("LIFECYCLE", "Progress " + uploadInfo.progressPercent)
            }

            override fun onSuccess(
                context: Context,
                uploadInfo: UploadInfo,
                serverResponse: ServerResponse
            ) {
                Log.e("LIFECYCLE", "Success " + uploadInfo.progressPercent)
                Log.e("LIFECYCLE", "server response is " + serverResponse.bodyString)

                newImageUrlLiveData.value = Gson().fromJson<UploadImageResponse>(
                    serverResponse.bodyString,
                    UploadImageResponse::class.java
                )
            }
        })
    }

    fun updateUserProfile(): LiveData<CustomResult<Any>> {
        return userRepo.updateUserProfile(
            viewModelScope,
            newUserName,
            newImageUrlLiveData.value?.address ?: ""
        )
    }
}