package ir.kindnesswall.utils.helper

import ir.kindnesswall.data.local.UserInfoPref

inline fun runIfAuthenticated(block: () -> Unit) {
    if (UserInfoPref.bearerToken.isNotEmpty()) {
        block()
    }
}