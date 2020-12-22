package com.happyWatter.codivAlarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.*
import java.io.IOException
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("SellerFirebaseService ", "Refreshed token :: $token")
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }




    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.i("SellerFirebaseService ", "Message :: $message")
//        sendGetRequest("d3pn725KT6WVgecH6KpW3E:APA91bGhHUKAlD6e1Zt1EVePHXIbjZmK5xazR602t8IeORCrgKlYsXo93oKmJuuqLBvU4-Dew8GHPocwvEAohyIwo7kAiISZQQq3FXGq-BFpJktSve3X33jNRF9flWsW3AdDGcu-5olo")


        val data = message.data
        val body = data["body"]
        val title  = data["title"]


        sendNotification(body!!, title!!)

    }

    private fun sendNotification(body: String, title: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("Notification", "dddddd")
        }

        val CHANNEL_ID = "CollocNotification"
        val CHANNEL_NAME = "CollocChannel"
        val description = "This is Colloc channel"
        val importance = NotificationManager.IMPORTANCE_HIGH

        var notificationManager: NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.setShowBadge(false)
            notificationManager.createNotificationChannel(channel)
        }

        var pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        var notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(notificationSound)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0, notificationBuilder.build())
    }

}