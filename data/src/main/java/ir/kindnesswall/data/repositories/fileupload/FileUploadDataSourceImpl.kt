package ir.kindnesswall.data.repositories.fileupload

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.webkit.MimeTypeMap
import com.bumptech.glide.Glide
import ir.kindnesswall.data.api.UserApi
import ir.kindnesswall.data.repositories.base.BaseDataSource
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.domain.entities.UploadImageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.coroutineScope
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FileUploadDataSourceImpl(
  private val userApi: UserApi
) : FileUploadDataSource , BaseDataSource() {
  override suspend fun uploadFile(context: Context, uri: Uri): UploadImageResponse =
    withContext(Dispatchers.IO) {
      val inFile = convertPublicContentToPrivateFile(context, uri)

      val failedResult = UploadImageResponse("").apply { isFailed = true }

      val normalizedBitmap = createNormalizedBitmap(inFile) ?: kotlin.run {
        Timber.w("uploadFile was skipped because of normalized bitmap was null. ")
        return@withContext failedResult
      }

      // write normalized bitmap on disk
      val normalizedFile = File.createTempFile("normalized_image", ".jpg", context.cacheDir)
      val os = BufferedOutputStream(FileOutputStream(normalizedFile))
      normalizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, os)

      val mediaType = (MimeTypeMap.getSingleton().getMimeTypeFromExtension(normalizedFile.extension)
        ?: "application/octet-stream"
        ).toMediaType()
      val imagePart = RequestBody.create(mediaType, normalizedFile)

      val request = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("image", inFile.name, imagePart)
        .build()

      // convert a flow to a blocking statement
      // TODO refactor after deprecating backoff or api call strategy
      val results: List<CustomResult<UploadImageResponse>> =
        getResultWithExponentialBackoffStrategy {
          userApi.uploadImage(request)
        }.toList()

      results.find { it.status == CustomResult.Status.SUCCESS }?.data ?: failedResult
    }

  override suspend fun convertPublicContentToPrivateFile(context: Context, uri: Uri): File {
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

  override suspend fun createNormalizedBitmap(inFile: File): Bitmap = coroutineScope {
    // estimate original bitmap size
    val option = BitmapFactory.Options()
    option.inJustDecodeBounds = true
    BitmapFactory.decodeFile(
      inFile.absolutePath,
      option
    ) // retrieve bitmap size and write on `option`

    // create normalized bitmap
    option.inSampleSize = calculateInSampleSize(option, 1000, 1000)
    option.inJustDecodeBounds = false

    BitmapFactory.decodeFile(inFile.absolutePath, option)
  }


  override suspend fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
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