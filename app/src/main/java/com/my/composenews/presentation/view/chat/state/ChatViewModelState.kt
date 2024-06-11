package com.my.composenews.presentation.view.chat.state

import android.view.SurfaceView
import android.view.TextureView
import com.my.composenews.domain.form.ChannelError

data class ChatViewModelState(
    val currentChannelId: String = "",
    val localView: Pair<Int, SurfaceView>? = null,
    val remoteView: Pair<Int, SurfaceView>? = null,
    val flip: Boolean = false,
    val speaker: Boolean = false,
    val camera: Boolean = false,
    val mute: Boolean = true,
    val shouldShowRemote: Boolean = false,
    val shouldShowLocal: Boolean = false,
    val token: String = "",
    val channelError: ChannelError = ChannelError()
){
    fun asCurrentChannelId() = currentChannelId

    fun asChannelError() = channelError

    fun showRemote() = shouldShowRemote

    fun showLocal() = camera

    fun isActiveFlip() = flip
    fun isActiveSpeaker() = speaker
    fun isActiveCamera() = camera
    fun isActiveMute() = mute
    fun asLocalView() = localView
    fun asRemoteView() = remoteView
}