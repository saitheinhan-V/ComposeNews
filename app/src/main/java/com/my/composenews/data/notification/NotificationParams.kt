package com.my.composenews.data.notification

import android.app.PendingIntent
import com.my.composenews.domain.vo.MessageVo

object NotificationParams {

    data class Message(
        val msg: MessageVo,
        val intent: PendingIntent?
    )
}