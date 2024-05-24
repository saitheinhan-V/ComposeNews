package com.my.composenews.data.repository

import com.my.composenews.domain.vo.MessageVo
import kotlinx.coroutines.flow.SharedFlow

interface PushRepository {

    val notificationMessage: SharedFlow<MessageVo>

    fun notifyMessage(message: MessageVo)
}