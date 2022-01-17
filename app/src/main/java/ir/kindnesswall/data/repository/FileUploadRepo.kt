package ir.kindnesswall.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import ir.kindnesswall.utils.wrapInBearer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import timber.log.Timber
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FileUploadRepo(context: Context) :
    BaseDataSource(context) {

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun uploadFile(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        imagePath: String,
        data: MutableLiveData<UploadImageResponse>
    ) {
        val outFile = withContext<File?>(Dispatchers.IO) {
            val inFile = if (imagePath.startsWith("content://"))
                convertPublicContentToPrivateFile(context, imagePath.toUri())
            else
                File(imagePath)

            val normalizedBitmap = createNormalizedBitmap(inFile) ?: kotlin.run {
                Timber.w("uploadFile was skipped because of normalized bitmap was null. ")
                return@withContext null
            }

            // write normalized bitmap on disk
            val normalizedFile = File.createTempFile("normalized_image", ".jpg", context.cacheDir)
            val os = BufferedOutputStream(FileOutputStream(normalizedFile))
            normalizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)

            normalizedFile
        } ?: return

        val request: MultipartUploadRequest =
            MultipartUploadRequest(context, serverUrl = "${BuildConfig.URL_WEBAPI}/image/upload")
                .setMethod("POST")
                .addFileToUpload(
                    filePath = outFile.absolutePath,
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

    private suspend fun createNormalizedBitmap(inFile: File): Bitmap? = coroutineScope {
        // estimate original bitmap size
        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
        BitmapFactory.decodeFile(inFile.absolutePath, option) // retrieve bitmap size and write on `option`

        // create normalized bitmap
        option.inSampleSize = calculateInSampleSize(option, 1000, 1000)
        option.inJustDecodeBounds = false

        BitmapFactory.decodeFile(inFile.absolutePath, option)
    }

    // source: https://developer.android.com/topic/performance/graphics/load-bitmap.html#load-bitmap
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}