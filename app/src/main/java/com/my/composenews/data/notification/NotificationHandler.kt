package com.my.composenews.data.notification

import android.app.Notification
import com.my.composenews.domain.vo.MessageVo


interface NotificationHandler {

    fun cancelAll()

    fun cancelByGroupId(id: String)

    fun show(notification: Notification)

    fun getNotificationByMessage(params: NotificationParams.Message): Notification
}