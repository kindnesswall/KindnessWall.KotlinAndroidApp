package ir.kindnesswall.view.main.conversation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ir.kindnesswall.data.model.TextMessageModel

class ConversationBroadcastReceiver(val listener: () -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        listener.invoke()
    }
}