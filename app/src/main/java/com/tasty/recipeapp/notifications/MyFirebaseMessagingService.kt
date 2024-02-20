package com.tasty.recipeapp.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.tasty.recipeapp.R
import com.tasty.recipeapp.model.response.NotificationExtraData
import com.tasty.recipeapp.ui.dashboardScreen.DashboardScreen
import com.tasty.recipeapp.ui.recipeDetails.RecipeDetailsScreen
import java.util.Random


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        var title = ""
        var desc = ""
        var image = ""
        var extraData = ""
        var pendingIntent: PendingIntent? = null

        if (remoteMessage.notification != null) {
            if (remoteMessage.notification!!.title != null) {
                title = remoteMessage.notification!!.title!!
            }

            if (remoteMessage.notification!!.body != null) {
                desc = remoteMessage.notification!!.body!!
            }

            val intent = Intent(this, DashboardScreen::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        }

        if (remoteMessage.data.isNotEmpty()) {
            saveNotification(remoteMessage.data)

            if (remoteMessage.data["title"] != null) {
                title = remoteMessage.data["title"]!!
            }

            if (remoteMessage.data["message"] != null) {
                desc = remoteMessage.data["message"]!!
            }

            if (remoteMessage.data["image"] != null) {
                image = remoteMessage.data["image"]!!
            }

            if (remoteMessage.data["extra_data"] != null) {
                extraData = remoteMessage.data["extra_data"]!!

                val notificationData = Gson().fromJson(extraData, NotificationExtraData::class.java)

                val intent1 = Intent(this, RecipeDetailsScreen::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                if (notificationData.firebase.isNotEmpty()) {
                    intent1.putExtra("id", notificationData.mealId)
                    intent1.putExtra("firebase", notificationData.firebase)
                }
                Log.d("DETAILSSSS", notificationData.mealId)
                pendingIntent =
                    PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_IMMUTABLE)

            } else {
                val intent2 = Intent(this, DashboardScreen::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                pendingIntent =
                    PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_IMMUTABLE)
            }

        }

        val builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(
                title
            )
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle())
            .setSound(
                RingtoneManager.getDefaultUri(
                    RingtoneManager.TYPE_NOTIFICATION
                )
            )
            .setAutoCancel(true)

        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = "Test Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        if (image.isNotEmpty()) {
            Glide.with(this)
                .asBitmap()
                .load(image)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        builder.setLargeIcon(resource)
                            .setStyle(
                                NotificationCompat.BigPictureStyle()
                                    .bigPicture(resource)
                            )

                        with(NotificationManagerCompat.from(this@MyFirebaseMessagingService)) {
                            // notificationId is a unique int for each notification that you must define.
                            if (ActivityCompat.checkSelfPermission(
                                    this@MyFirebaseMessagingService,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                return
                            }
                            notify(Random().nextInt(10000), builder.build())
                        }

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                    }
                })

        }


        if (image.isEmpty()) {
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define.
                if (ActivityCompat.checkSelfPermission(
                        this@MyFirebaseMessagingService,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                notify(Random().nextInt(10000), builder.build())
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FTOKENNEW", token)

    }

    private fun saveNotification(data: Map<String,String>) {
        val firebase = Firebase.firestore
        val auth = Firebase.auth

        firebase.collection("users").document(auth.currentUser!!.uid).collection("notifications")
            .add(data).addOnSuccessListener {

        }.addOnFailureListener {

        }

    }
}
