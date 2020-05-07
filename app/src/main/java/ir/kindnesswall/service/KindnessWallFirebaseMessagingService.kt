package ir.kindnesswall.service

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.model.TextMessageModel
import ir.kindnesswall.utils.NotificationHandler


class KindnessWallFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var title: String? = ""
        var message: String? = ""
        var uri: String? = ""

        var model: Any? = null

        remoteMessage.data.let {
            if (!it.isNullOrEmpty()) {

                uri = it["click_action"]
                title = resources.getString(R.string.new_message)

                val jsonString = it["message"]

                model = try {
                    Gson().fromJson(jsonString, TextMessageModel::class.java)
                } catch (e: Exception) {
                    null
                }

                if (model != null && model is TextMessageModel) {
                    message = (model as TextMessageModel).text.toString()
                }
            }
        }

        if (AppPref.isAppInForeground && AppPref.isInChatPage && model != null && model is TextMessageModel) {
            val intent = Intent()
            intent.action = "CHAT"
            intent.putExtra("textMessageModel", model as TextMessageModel)
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            sendBroadcast(intent)
            return
        }

        NotificationHandler.sendNotificationViaDeepLink(this, title, message, uri, model)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        UserInfoPref.fireBaseToken = token
        AppPref.shouldUpdatedFireBaseToken = true
    }
}