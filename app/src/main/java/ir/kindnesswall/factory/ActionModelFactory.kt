package ir.kindnesswall.factory

import android.content.Context
import android.content.Intent
import android.net.Uri
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
                return getIntent(context, ChatActivity::class.java)
                    .putExtra("chatId", (model as TextMessageModel).chatId)
            }
        }

        return Intent().apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
    }

    private fun getIntent(context: Context, activity: Class<*>): Intent {
        return Intent(context, activity).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }
}