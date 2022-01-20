package ir.kindnesswall.view.main.addproduct

import android.net.Uri

/**
 * Created by Farhad Beigirad on 1/18/22.
 */
sealed class GiftImage {
    data class LocalImage(val uri: Uri) : GiftImage()
    data class OnlineImage(val url: String) : GiftImage()
}