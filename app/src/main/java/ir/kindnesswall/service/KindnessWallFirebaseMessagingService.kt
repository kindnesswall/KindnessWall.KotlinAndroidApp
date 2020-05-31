package ir.kindnesswall.service

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ir.kindnesswall.KindnessApplication
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.model.TextMessageModel
import ir.kindnesswall.data.repository.ChatRepo
import ir.kindnesswall.utils.NotificationHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class KindnessWallFirebaseMessagingService : FirebaseMessagingService() {
    private val chatRepo: ChatRepo by inject()

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

        if (AppPref.isAppInForeground && model != null && model is TextMessageModel) {
            if (AppPref.isInChatPage && (model as TextMessageModel).chatId == AppPref.currentChatSessionId) {
                val intent = Intent()
                intent.action = "CHAT"
                intent.putExtra("textMessageModel", model as TextMessageModel)
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                sendBroadcast(intent)
                return
            } else {
                val contact =
                    KindnessApplication.instance.getContact((model as TextMessageModel).chatId)

                if (contact == null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val contactList = chatRepo.getConversationList()
                        if (contactList.isNullOrEmpty()) {
                            return@launch
                        }

                        KindnessApplication.instance.setContactList(contactList)

                        val intent = Intent()
                        intent.action = "NEW_CONTACT_LIST"
                        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                        sendBroadcast(intent)
                    }
                } else {
                    contact.notificationCount++
                    KindnessApplication.instance.updateContactList(contact)
                    val intent = Intent()
                    intent.action = "UPDATE_CONTACT_LIST"
                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                    sendBroadcast(intent)
                }
            }
        }

        NotificationHandler.sendNotificationViaDeepLink(this, title, message, uri, model)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        UserInfoPref.fireBaseToken = token
        AppPref.shouldUpdatedFireBaseToken = true
    }
}