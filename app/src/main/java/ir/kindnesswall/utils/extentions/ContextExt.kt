package ir.kindnesswall.utils.extentions

import android.content.Context
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.view.authentication.AuthenticationActivity

fun Context.runOrStartAuth(block: () -> Unit) {
    if (UserInfoPref.bearerToken.isEmpty()) {
        AuthenticationActivity.start(this)
    } else {
        block.invoke()
    }
}