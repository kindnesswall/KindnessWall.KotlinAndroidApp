package ir.kindnesswall.view.main.conversation.chat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ir.kindnesswall.domain.entities.TextMessageModel

class ChatBroadcastReceiver(val listener: (TextMessageModel) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val model = intent?.getSerializableExtra("textMessageModel") as TextMessageModel
        model.let { listener.invoke(it) }
    }
}