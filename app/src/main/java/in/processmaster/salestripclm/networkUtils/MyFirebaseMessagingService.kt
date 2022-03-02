package `in`.processmaster.salestripclm.networkUtils

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SplashActivity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.IOException
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var numMessages = 0
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("onMessageReceived", "meessage")
        val notification = remoteMessage.notification
        val data = remoteMessage.data
        sendNotification(notification, data)
        Handler(Looper.getMainLooper()).post {
            // Toast.makeText(getApplicationContext(), "hfisgfuisgfsdfsf sfsdfsdf ssdfsfsdfewtwefsdfsdfs ewfsfs fwer sdf", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onNewToken(token: String) {
        Log.e("onNewToken", "Refreshed token: $token")
    }

    private fun sendNotification(
        notification: RemoteMessage.Notification?,
        data: Map<String, String>
    ) {
        val bundle = Bundle()
        bundle.putString(
            FCM_PARAM,
            data[FCM_PARAM]
        )
        val intent = Intent(this, SplashActivity::class.java)
        intent.putExtras(bundle)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationBuilder = NotificationCompat.Builder(this, "default")
            .setContentTitle(data["title"])
            .setContentText(data["message"])
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
            .setContentIntent(pendingIntent)
            .setContentInfo("Hello")
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setLights(Color.RED, 1000, 300)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setNumber(++numMessages)
            .setSmallIcon(R.drawable.salestrip_final_logo_brochure)
        try {
            val picture = data[FCM_PARAM]
            if (picture != null && "" != picture) {
                val url = URL(picture)
                val bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(
                        notification!!.body
                    )
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default", CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESC
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(channel)
        }
        assert(notificationManager != null)
        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        const val FCM_PARAM = "picture"
        private const val CHANNEL_NAME = "FCM"
        private const val CHANNEL_DESC = "Firebase Cloud Messaging"
    }
}
