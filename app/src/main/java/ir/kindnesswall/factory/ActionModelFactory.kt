package ir.kindnesswall.factory

import android.content.Context
import android.content.Intent
import ir.kindnesswall.view.main.conversation.chat.ChatActivity

object ActionModelFactory {
    fun getActionIntent(context: Context, uri: String?): Intent {
        if (uri.isNullOrEmpty()) {
            return Intent().apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
        }

        when (uri) {
            "chat" -> {
                return Intent(context, ChatActivity::class.java)
            }
        }
//        when (uri.authority) {
//            ActionType.OPEN_MASSEUR_DETAILS_TEMP -> {
//                return getIntent(
//                    context,
//                    ClientDetailsActivity::class.java
//                ).apply {
//                    putExtra("clientId", uri.getQueryParameter("masseur_id"))
//                }
//            }
//
//            ActionType.OPEN_REQUESTS_PAGE_TEMP -> {
//                return getIntent(
//                    context,
//                    RequestDetailsActivity::class.java
//                ).apply {
//                    putExtra("requestId", uri.getQueryParameter("request_id"))
//                }
//            }
//
//            ActionType.OPEN_UPLOAD_PHOTO_TEMP -> {
//                return getIntent(
//                    context,
//                    EditPhotosActivity::class.java
//                )
//            }
//
//            ActionType.OPEN_EDIT_PROFILE_TEMP -> {
//                return getIntent(
//                    context,
//                    EditProfileActivity::class.java
//                )
//            }
//
//            ActionType.OPEN_CHAT_CONVERSATION -> {
//                return getIntent(
//                    context,
//                    ChatActivity::class.java
//                ).apply {
//                    putExtra("chatId", uri.getQueryParameter("chatId"))
//                    putExtra("userId", uri.getQueryParameter("userId"))
//                }
//            }
//
//        }

        return Intent().apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
    }

    private fun getIntent(context: Context, activity: Class<*>): Intent {
        return Intent(context, activity).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }
}