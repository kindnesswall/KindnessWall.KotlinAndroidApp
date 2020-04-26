package ir.kindnesswall.service

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.model.TextMessageModel
import ir.kindnesswall.utils.NotificationHandler


class KindnessWallFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var title = ""
        var message = ""
        var uri = ""

        remoteMessage.data.let {
            if (it.isNullOrEmpty()) {
                title = it["title"] ?: ""
                message = it["body"] ?: ""
                uri = it["click_action"] ?: ""
            }
        }

        if (AppPref.isAppInForeground) {

            val textMessageModel = TextMessageModel()
            val intent = Intent()
            intent.action = "CHAT"
            intent.putExtra("textMessageModel", textMessageModel)
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            sendBroadcast(intent)
            return
        }

        NotificationHandler.sendNotificationViaDeepLink(this, title, message, uri)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        UserInfoPref.fireBaseToken = token
        AppPref.shouldUpdatedFireBaseToken = true
    }
}