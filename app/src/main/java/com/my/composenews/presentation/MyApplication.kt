package com.my.composenews.presentation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.app
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.my.composenews.R
import com.my.composenews.data.AppConstant
import com.my.composenews.data.agora.AgoraManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        createNotificationChannel()

        FirebaseMessaging.getInstance().token.addOnCompleteListener {task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
            Log.i("push.data", "Token $token")
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            val name = AppConstant.MSG_CHANNEL_NAME
            val descriptionText = ""
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(AppConstant.MSG_CHANNEL_ID,name,importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}