package ir.kindnesswall.data.repositories.fileupload

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import ir.kindnesswall.domain.entities.UploadImageResponse
import java.io.File

interface FileUploadDataSource {

  suspend fun uploadFile(context: Context, uri: Uri): UploadImageResponse

  suspend fun convertPublicContentToPrivateFile(context: Context, uri: Uri): File

  suspend fun createNormalizedBitmap(inFile: File): Bitmap?

  suspend fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int,
    reqHeight: Int
  ): Int


}