package com.my.composenews.data.notification

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.my.composenews.R
import com.my.composenews.data.AppConstant
import com.my.composenews.data.dispatcher.DispatcherModule
import com.my.composenews.domain.vo.MessageVo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class NotificationHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @DispatcherModule.IoScope ioScope: CoroutineScope
): NotificationHandler {

    private val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun buildNotification(
        context: Context,
        channelId: String,
        init: NotificationCompat.Builder.() -> Unit
    ): Notification {
        val builder = NotificationCompat.Builder(context,channelId)
        builder.init()
        return builder.build()
    }

    override fun cancelAll() {
        manager.cancelAll()
    }

    override fun cancelByGroupId(id: String) {
        val activeNotifications = manager.activeNotifications
        activeNotifications.forEach {
            if(it.notification.group == id){
                manager.cancel(it.id)
            }
        }
    }

    override fun show(notification: Notification) {
        val notificationId = System.currentTimeMillis().toInt()
        manager.notify(notificationId,notification)
    }

    override fun getNotificationByMessage(params: NotificationParams.Message): Notification {
        return buildNotification(
            channelId = AppConstant.MSG_CHANNEL_ID,
            context = context,
            init = {
                setSmallIcon(R.drawable.ic_small_notification)
                setGroup(params.msg.uid)
                setAutoCancel(true)
                setContentTitle(params.msg.name)
                setContentText("${params.msg.name} : ${params.msg.age}")
                priority = NotificationCompat.PRIORITY_HIGH // to support Android 7.0 and lower
                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                setCategory(NotificationCompat.CATEGORY_MESSAGE)
                setContentIntent(params.intent)
            }
        )
    }
}