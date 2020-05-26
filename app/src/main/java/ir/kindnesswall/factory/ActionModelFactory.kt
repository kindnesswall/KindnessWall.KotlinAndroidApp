package ir.kindnesswall.factory

import android.content.Context
import android.content.Intent
import android.net.Uri
import ir.kindnesswall.KindnessApplication
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.model.ChatModel
import ir.kindnesswall.data.model.TextMessageModel
import ir.kindnesswall.view.main.conversation.chat.ChatActivity
import ir.kindnesswall.view.splash.SplashActivity

object ActionModelFactory {
    fun getActionIntent(context: Context, uri: String?, model: Any?): Intent {
        if (uri.isNullOrEmpty() && model == null) {
            return getIntent(context, SplashActivity::class.java)
        }

        val url = Uri.parse(uri)

        when (url.authority) {
            "chat" -> {
                if (AppPref.isAppInForeground) {
                    var requestChatModel = ChatModel()
                    requestChatModel.chatId = (model as TextMessageModel).chatId
                    requestChatModel.contactId = model.senderId
                    requestChatModel.userId = model.senderId

                    return getIntent(context, ChatActivity::class.java)
                        .putExtra("isStartFromNotification", false)
                        .putExtra("requestChatModel", requestChatModel)
                        .putExtra("isCharity", true)
                } else {
                    val contact =
                        KindnessApplication.instance.getContact((model as TextMessageModel).chatId)

                    return if (contact == null) {
                        var requestChatModel = ChatModel()
                        requestChatModel.chatId = (model as TextMessageModel).chatId
                        requestChatModel.contactId = (model as TextMessageModel).senderId
                        requestChatModel.userId = (model as TextMessageModel).senderId

                        getIntent(context, SplashActivity::class.java)
                            .putExtra("isStartFromNotification", true)
                            .putExtra("requestChatModel", requestChatModel)
                    } else {
                        getIntent(context, ChatActivity::class.java)
                            .putExtra("isStartFromNotification", false)
                            .putExtra("chatContactModel", contact)
                            .putExtra("isCharity", contact.contactProfile?.isCharity ?: false)
                    }
                }
            }
        }

        return getIntent(context, SplashActivity::class.java)
    }

    private fun getIntent(context: Context, activity: Class<*>): Intent {
        return Intent(context, activity).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }
}