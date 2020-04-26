package ir.kindnesswall.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import ir.kindnesswall.BuildConfig
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.model.BaseDataSource
import ir.kindnesswall.data.model.UploadImageResponse
import ir.kindnesswall.data.remote.network.UploadFileApi
import ir.kindnesswall.utils.wrapInBearer
import com.google.gson.Gson
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest


class FileUploadRepo(val context: Context, private val uploadFileApi: UploadFileApi) :
    BaseDataSource() {
    
    fun uploadFile(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        imagePath: String,
        data: MutableLiveData<UploadImageResponse>
    ) {
        val request: MultipartUploadRequest =
            MultipartUploadRequest(context, serverUrl = "${BuildConfig.URL_WEBAPI}/image/upload")
                .setMethod("POST")
                .addFileToUpload(filePath = imagePath, parameterName = "image")

        request.addHeader("Authorization", wrapInBearer(UserInfoPref.bearerToken))

        request.subscribe(context, lifecycleOwner, object : RequestObserverDelegate {
            override fun onCompleted(context: Context, uploadInfo: UploadInfo) {
                Log.e("LIFECYCLE", "Completed ")
            }

            override fun onCompletedWhileNotObserving() {
            }

            override fun onError(context: Context, uploadInfo: UploadInfo, exception: Throwable) {
                Log.e("LIFECYCLE", "Error " + exception.message)
                data.value = UploadImageResponse("")
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

                data.value = Gson().fromJson<UploadImageResponse>(
                    serverResponse.bodyString,
                    UploadImageResponse::class.java
                )
            }
        })
    }
}