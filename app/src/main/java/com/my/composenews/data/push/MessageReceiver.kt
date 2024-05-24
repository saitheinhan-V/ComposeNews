package com.my.composenews.data.push

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.my.composenews.data.dispatcher.DispatcherModule
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MessageReceiver: FirebaseMessagingService() {

    @Inject
    @DispatcherModule.IoScope
    lateinit var ioScope: CoroutineScope

    @Inject
    lateinit var pushServiceHandler: PushServiceHandler

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("push.data","Token $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val data = message.data
        Log.i("push.data","Response $data")
        ioScope.launch {
            data.let {
                pushServiceHandler.onReceived(data,this@MessageReceiver)
            }
        }
    }
}