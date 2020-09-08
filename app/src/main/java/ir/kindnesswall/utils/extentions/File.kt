package ir.kindnesswall.utils.extentions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * @return reduced size file path address
 */
fun File.reduceImageSizeAndSave(context: Context): String? {
    return try {
        // BitmapFactory options to downsize the image
        val file = File(this.path)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        options.inSampleSize = 4
        // factor of downsizing the image
        var inputStream = FileInputStream(file)
        //Bitmap selectedBitmap = null;
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream.close()

        // The new size we want to scale to
        val reduceSizePercent = 75

        // Find the correct scale value. It should be the power of 2.
        var scale = 1
        while (options.outWidth / scale / 2 >= reduceSizePercent &&
            options.outHeight / scale / 2 >= reduceSizePercent
        ) {
            scale *= 2
        }
        val newOptions = BitmapFactory.Options()
        newOptions.inSampleSize = scale
        inputStream = FileInputStream(file)
        val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, newOptions)
        inputStream.close()

        // here i override the original image file
        val rootDir = File(context.filesDir.path + File.separator + "Photos")
        val newFile =
            File(rootDir.path + File.separator + file.name)
        if (!rootDir.exists())
            rootDir.mkdir()

        newFile.createNewFile()
        val outputStream = FileOutputStream(newFile)
        selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return newFile.path
    } catch (e: Exception) {
        null
    }
}