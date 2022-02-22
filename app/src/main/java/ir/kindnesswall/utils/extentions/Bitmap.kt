package ir.kindnesswall.utils.extentions

import android.graphics.*

/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */
fun Bitmap.toCircle(): Bitmap {
    val output = Bitmap.createBitmap(
        this.width,
        this.height, Bitmap.Config.ARGB_8888
    )
    try {

        val canvas = Canvas(output)

        val color = Color.RED
        val paint = Paint()
        val rect = Rect(0, 0, this.width, this.height)
        val rectF = RectF(rect)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawOval(rectF, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(this, rect, rect, paint)

    } catch (e: Exception) {
        return this
    }

    return output
}