package ir.kindnesswall.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import ir.kindnesswall.R
import ir.kindnesswall.utils.extentions.dp
import timber.log.Timber
import java.io.IOException

/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage: a file to add some general useful utils like share
 *
 * How to call: just call functions
 */

/**
 * share string with other apps
 *
 * @param [context] to create share intent
 * @param [message] to share a apiResponseMessage with others, it's our share body
 * @param [subject] the title of share event that shown in bottom menu of native android
 *
 * */
fun shareString(
    context: Context,
    message: String,
    subject: String = "",
    shareTitle: String = "Share via:"
) {
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "text/plain"

    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
    sharingIntent.putExtra(Intent.EXTRA_TEXT, message)

    context.startActivity(Intent.createChooser(sharingIntent, shareTitle))
}

fun getRoundBottomSheet(
    context: Context,
    layoutId: Int,
    layoutInflater: LayoutInflater
): BottomSheetDialog {
    val bottomSheetView = layoutInflater.inflate(layoutId, null)

    val dialog = BottomSheetDialog(context)

    dialog.setOnShowListener {
        val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val coordinatorLayout = bottomSheet!!.parent as CoordinatorLayout
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.peekHeight = bottomSheet.height
        coordinatorLayout.parent.requestLayout()
    }

    dialog.setContentView(bottomSheetView)

    val backgroundView: View? = dialog.window?.findViewById(R.id.design_bottom_sheet)

    backgroundView?.let { view ->
        view.setBackgroundResource(android.R.color.transparent)
        (view.layoutParams as CoordinatorLayout.LayoutParams).marginStart =
            12.dp(context)
        (view.layoutParams as CoordinatorLayout.LayoutParams).marginEnd =
            12.dp(context)
    }

    return dialog
}

fun isNextPageAvailable(selfPageUrl: String? = "", nextPageUrl: String? = ""): Boolean {
    return !selfPageUrl.isNullOrEmpty() && selfPageUrl != nextPageUrl
}

fun wrapInBearer(token: String): String {
    return if (token.isNotEmpty() && !token.startsWith("Bearer"))
        "Bearer $token"
    else
        token
}

fun gotoPlayStore(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("market://details?id=${context.packageName}")
    startActivity(context, intent, null)
}

fun rateToApp(context: Context) {
    gotoPlayStore(context)
}

fun updateApp(context: Context) {
    gotoPlayStore(context)
}

fun isLocationPermissionGranted(context: Context?): Boolean {
    return context != null && ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

/**
 * Fix Bitmap orientation issue
 **/
@Throws(IOException::class)
fun modifyOrientation(bitmap: Bitmap, image_absolute_path: String?): Bitmap {
    if (image_absolute_path == null) return bitmap
    try {
        val ei = ExifInterface(image_absolute_path)
        val orientation =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate(
                bitmap,
                90f
            )

            ExifInterface.ORIENTATION_ROTATE_180 -> rotate(
                bitmap,
                180f
            )

            ExifInterface.ORIENTATION_ROTATE_270 -> rotate(
                bitmap,
                270f
            )

            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(
                bitmap,
                horizontal = true,
                vertical = false
            )

            ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(
                bitmap,
                horizontal = false,
                vertical = true
            )

            else -> bitmap
        }
    } catch (e: java.lang.Exception) {
        return bitmap
    }
}

fun rotate(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap {
    val matrix = Matrix()
    matrix.preScale((if (horizontal) -1 else 1).toFloat(), (if (vertical) -1 else 1).toFloat())
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun openSupportForm(context: Context) {
    openUrl(context, "http://t.me/Kindness_Wall_Admin")
}

fun openTermsAndConditionLink(context: Context) {
    openUrl(context, "https://kindnesswand.com/terms")
}

fun openUrl(context: Context, url: String) {
    runCatching {
        context.startActivity(
            Intent(Intent.ACTION_VIEW).apply {
                setDataAndNormalize(Uri.parse(url))
            }
        )
    }.onFailure {
        Timber.d(it, "can't open link")
        Toast.makeText(context, context.getString(R.string.related_app_not_found), Toast.LENGTH_SHORT).show()
    }
}