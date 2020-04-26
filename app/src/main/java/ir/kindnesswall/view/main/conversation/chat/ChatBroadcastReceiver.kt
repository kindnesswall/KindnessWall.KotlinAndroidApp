package ir.kindnesswall.view.main.conversation.chat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ir.kindnesswall.data.model.TextMessageModel

class ChatBroadcastReceiver(val listener: (TextMessageModel) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.getParcelableExtra<TextMessageModel>("TextMessage")?.let { listener.invoke(it) }
    }
}