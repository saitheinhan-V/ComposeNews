package com.my.composenews.data.repository

import com.my.composenews.data.dispatcher.DispatcherModule
import com.my.composenews.domain.vo.MessageVo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PushRepositoryImpl @Inject constructor(
    @DispatcherModule.IoScope private val ioScope: CoroutineScope
): PushRepository{

    private val mNotificationMessage = MutableSharedFlow<MessageVo>()
    override val notificationMessage: SharedFlow<MessageVo>
        get() = mNotificationMessage.asSharedFlow()

    override fun notifyMessage(message: MessageVo) {
        ioScope.launch {
            mNotificationMessage.emit(message)
        }
    }

}