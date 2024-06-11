package com.my.composenews.data.agora

import android.content.Context
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine

object AgoraManager {
    private var rtcEngine: RtcEngine? = null

    fun initialize(
        context: Context,
        appId: String,
        handler: IRtcEngineEventHandler
    ): RtcEngine? {
        if(rtcEngine != null) return rtcEngine
        try {
            rtcEngine = RtcEngine.create(context, appId, handler)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rtcEngine
    }

    fun getEngine(): RtcEngine? = rtcEngine

    fun destroy() {
        rtcEngine?.leaveChannel()
        RtcEngine.destroy()
        rtcEngine = null
    }
}
