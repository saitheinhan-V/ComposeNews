package com.my.composenews.data.agora

import android.view.SurfaceView
import android.view.TextureView
import kotlinx.coroutines.flow.SharedFlow

interface AgoraHandler {

    val agoraParams: SharedFlow<AgoraParams>
    fun setupProfile()
    fun setupVideo()
    fun setupLocalView(id: Int): Pair<Int, SurfaceView>
    fun setupRemoteView(id: Int): Pair<Int, SurfaceView>
    fun muteLocalVideo(muted: Boolean)
    fun muteRemoteVideo(id: Int, muted: Boolean)
    fun enabledSpeaker(enabled: Boolean)
    fun flipCamera(enabled: Boolean)
    fun muteLocalAudio(muted: Boolean)
    fun joinChannel(
        token: String,
        channelName: String,
        optionalInfo: String,
        uid: Int
    )

    fun leaveChannel(
        channelName: String
    )

    fun destroy()
}