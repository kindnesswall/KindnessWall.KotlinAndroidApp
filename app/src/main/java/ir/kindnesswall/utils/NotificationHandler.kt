package ir.kindnesswall.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ir.kindnesswall.R
import ir.kindnesswall.factory.ActionModelFactory

object NotificationHandler {

    fun sendNotificationViaDeepLink(
        context: Context,
        title: String?,
        content: String?,
        url: String?,
        model: Any?
    ) {
        var pIntent: PendingIntent? = null

        if (!url.isNullOrEmpty()) {
            val intent = ActionModelFactory.getActionIntent(context, url, model)
            pIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder = NotificationCompat.Builder(context, "Notifications")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.priority = NotificationManager.IMPORTANCE_HIGH
        } else {
            builder.priority = Notification.PRIORITY_HIGH
        }

        builder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_notification_small)
            .setColor(ContextCompat.getColor(context, R.color.notification_small_color))
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setContentText(content)
            .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
            .setCategory("Feeds")
            .setContentInfo("Notification")
            .also {
                if (pIntent != null) {
                    it.setContentIntent(pIntent)
                }
            }

        notifyMessage(context, builder)
    }

    private fun notifyMessage(context: Context, builder: NotificationCompat.Builder) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelID = "Notifications"// The id of the channel.
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelID, "Notifications", importance)

            // Create a notification and set the notification channel.
            val notification = builder
                .setChannelId(channelID)
                .build()

            notificationManager.createNotificationChannel(mChannel)
            notificationManager.notify(12312, notification)
        } else {
            notificationManager.notify(
                12312 /* ID of notification */,
                builder.build()
            )
        }
    }
}