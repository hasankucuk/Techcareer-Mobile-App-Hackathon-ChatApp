package com.techcareer.mobileapphackathon.chatapp.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.techcareer.mobileapphackathon.chatapp.R
import com.techcareer.mobileapphackathon.chatapp.ui.splash.SplashScreen

object NotificationUtils {

    private val VERBOSE_CHANNEL_NAME: CharSequence = "VoiceMessage"
    private const val VERBOSE_CHANNEL_DESCRIPTION = ""
    private const val CHANNEL_ID = "VoiceMessage Notification"

    fun makeStatusNotification(
        context: Context,
        newNotification: NewMessageNotification
    ) {
        createNotificationChannel(context)
        val notification = getNotification(context, newNotification)
        NotificationManagerCompat.from(context).notify(newNotification.notificationId, notification)
    }

    private fun getNotification(
        context: Context,
        notification: NewMessageNotification
    ): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(getPendingIntent(context))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(notification.senderName)
            .setContentText(notification.message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))
            .build()
    }

    private fun getPendingIntent(context: Context): PendingIntent? {
        val resultIntent = Intent(context, SplashScreen::class.java)
        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            } else {
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }

     fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = VERBOSE_CHANNEL_NAME
//            val description = VERBOSE_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
//            channel.description = description
            getNotificationManager(context)?.createNotificationChannel(channel)
        }
    }

    private fun getNotificationManager(context: Context): NotificationManager? {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
    }
}