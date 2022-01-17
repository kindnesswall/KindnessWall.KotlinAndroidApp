package ir.kindnesswall.data.repository

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import ir.kindnesswall.BuildConfig
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.model.BaseDataSource
import ir.kindnesswall.data.model.UploadImageResponse
import ir.kindnesswall.utils.extentions.reduceImageSizeAndSave
import ir.kindnesswall.utils.wrapInBearer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FileUploadRepo(context: Context) :
    BaseDataSource(context) {

    suspend fun uploadFile(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        imagePath: String,
        data: MutableLiveData<UploadImageResponse>
    ) {
        var inFile = File(imagePath)
        if (imagePath.startsWith("content://")) {
            withContext(Dispatchers.IO) {
                val uri = imagePath.toUri()
                inFile = convertPublicContentToPrivateFile(context, uri)
            }
        }

        val request: MultipartUploadRequest =
            MultipartUploadRequest(context, serverUrl = "${BuildConfig.URL_WEBAPI}/image/upload")
                .setMethod("POST")
                .addFileToUpload(
                    filePath = inFile.reduceImageSizeAndSave(context)!!,
                    parameterName = "image"
                )

        request.addHeader("Authorization", wrapInBearer(UserInfoPref.bearerToken))

        request.subscribe(context, lifecycleOwner, object : RequestObserverDelegate {
            override fun onCompleted(context: Context, uploadInfo: UploadInfo) {
                Log.e("LIFECYCLE", "Completed ")
            }

            override fun onCompletedWhileNotObserving() {
            }

            override fun onError(context: Context, uploadInfo: UploadInfo, exception: Throwable) {
                Log.e("LIFECYCLE", "Error " + exception.message)
                data.value = UploadImageResponse("").apply { isFailed = true }
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
                ).apply { isFailed = false }
            }
        })
    }

    private suspend fun convertPublicContentToPrivateFile(context: Context, uri: Uri): File {
        return suspendCoroutine {
            Glide.with(context)
                .downloadOnly()
                .load(uri)
                .into(object : com.bumptech.glide.request.target.CustomTarget<File>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                        it.resume(resource)
                    }
                })
        }
    }
}