package com.my.composenews.data.agora

import android.content.Context
import android.util.Log
import android.view.SurfaceView
import com.my.composenews.data.AppConstant
import com.my.composenews.data.dispatcher.DispatcherModule
import dagger.hilt.android.qualifiers.ApplicationContext
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas
import io.agora.rtc2.video.VideoEncoderConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AgoraHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @DispatcherModule.IoScope private val ioScope: CoroutineScope
) : AgoraHandler {

    private val params = MutableSharedFlow<AgoraParams>()
    override val agoraParams: SharedFlow<AgoraParams>
        get() = params

    private var rtcEngine: RtcEngine? = null

    private val eventHandler = object : IRtcEngineEventHandler() {
        override fun onError(err: Int) {
            super.onError(err)
//            when (err) {
//                ErrorCode.ERR_TOKEN_EXPIRED -> sendMessage("Your token has expired")
//                ErrorCode.ERR_INVALID_TOKEN -> sendMessage("Your token is invalid")
//                else -> sendMessage("Error code: $err")
//            }
        }

        //local user leave
        override fun onLeaveChannel(stats: RtcStats?) {
            super.onLeaveChannel(stats)
        }

        override fun onRequestToken() {
            super.onRequestToken()
        }

        //remote user joined
        override fun onUserJoined(uid: Int, elapsed: Int) {
            super.onUserJoined(uid, elapsed)
            ioScope.launch {
                params.emit(AgoraParams.OnJoinedOtherUsers(uid))
            }
        }

        override fun onRejoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            super.onRejoinChannelSuccess(channel, uid, elapsed)
            ioScope.launch {
                params.emit(AgoraParams.OnJoinedOtherUsers(uid))
            }
        }

        //local user joined
        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            ioScope.launch {
                params.emit(AgoraParams.OnJoinedCurrentUser(uid))
            }
        }

        //remote user leave
        override fun onUserOffline(uid: Int, reason: Int) {
            super.onUserOffline(uid, reason)
            ioScope.launch {
                params.emit(AgoraParams.OnOfflineUser(uid))
            }
        }

        override fun onActiveSpeaker(uid: Int) {
            super.onActiveSpeaker(uid)
            ioScope.launch {
            }
        }

        override fun onUserMuteAudio(uid: Int, muted: Boolean) {
            super.onUserMuteAudio(uid, muted)
            ioScope.launch {
                params.emit(AgoraParams.OnMuteAudio(uid, muted))
            }
        }

        override fun onUserMuteVideo(uid: Int, muted: Boolean) {
            super.onUserMuteVideo(uid, muted)
            ioScope.launch {
                params.emit(AgoraParams.OnMuteVideo(uid, muted))
            }
        }

        override fun onRtcStats(stats: RtcStats?) {
            super.onRtcStats(stats)
            Log.d("dart.onRtcStats", "${stats?.totalDuration}")
        }

        override fun onConnectionStateChanged(state: Int, reason: Int) {
            super.onConnectionStateChanged(state, reason)
//            if (state == Constants.CONNECTION_STATE_FAILED && reason == Constants.CONNECTION_CHANGED_BANNED_BY_SERVER) {
//                scope.launch {
//                    params.emit(AgoraParams.Decline)
//                }
//            }
        }

        override fun onRemoteAudioStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
            super.onRemoteAudioStateChanged(uid, state, reason, elapsed)
        }

        override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
            super.onRemoteVideoStateChanged(uid, state, reason, elapsed)
        }

        override fun onLocalAudioStateChanged(state: Int, error: Int) {
            super.onLocalAudioStateChanged(state, error)
        }

        override fun onLocalVideoStateChanged(
            source: Constants.VideoSourceType?,
            state: Int,
            error: Int
        ) {
            super.onLocalVideoStateChanged(source, state, error)
        }
    }

    override fun setupProfile() {
        rtcEngine = AgoraManager.initialize(
            context,
            appId = AppConstant.AGORA_APP_ID,
            handler = eventHandler
        )

        rtcEngine?.apply {
            this.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)
            this.setClientRole(Constants.CLIENT_ROLE_BROADCASTER)
            this.enableVideo()
            this.enableAudio()
        }

    }

    override fun setupVideo() {
        rtcEngine?.apply {
            this.enableVideo()
            this.enableAudioVolumeIndication(50, 10, true)
            this.setVideoEncoderConfiguration(
                VideoEncoderConfiguration(
                    VideoEncoderConfiguration.VD_640x360,
                    VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                    VideoEncoderConfiguration.STANDARD_BITRATE,
                    VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
                )
            )
        }
    }

    override fun setupLocalView(id: Int): Pair<Int, SurfaceView> {
        val view = SurfaceView(context)
        view.apply {
            tag = id
            setZOrderMediaOverlay(true)
        }
        rtcEngine?.apply {
            this.setupLocalVideo(
                VideoCanvas(
                    view,
                    VideoCanvas.RENDER_MODE_HIDDEN,
                    id
                )
            )
            this.startPreview()
            this.muteLocalVideoStream(true)
        }.also {
            return Pair(id, view)
        }
    }

    override fun setupRemoteView(id: Int): Pair<Int, SurfaceView> {
        val view = SurfaceView(context)
        view.apply {
            tag = id
        }
        rtcEngine?.apply {
            this.setupRemoteVideo(
                VideoCanvas(
                    view,
                    Constants.RENDER_MODE_HIDDEN,
                    id
                )
            )
            this.startPreview(Constants.VideoSourceType.VIDEO_SOURCE_REMOTE)
            this.muteRemoteVideoStream(id, true)
        }.also {
            return Pair(id, view)
        }
    }

    override fun muteLocalVideo(muted: Boolean) {
        rtcEngine?.apply {
            this.muteLocalVideoStream(muted)
        }
    }

    override fun muteRemoteVideo(id: Int, muted: Boolean) {
        rtcEngine?.apply {
            this.muteRemoteVideoStream(id, muted)
        }
    }

    override fun enabledSpeaker(enabled: Boolean) {
        rtcEngine?.apply {
            this.setEnableSpeakerphone(enabled)
        }
    }

    override fun flipCamera(enabled: Boolean) {
        rtcEngine?.apply {
            this.switchCamera()
        }
    }

    override fun muteLocalAudio(muted: Boolean) {
        rtcEngine?.apply {
            this.muteLocalAudioStream(muted)
        }
    }

    override fun joinChannel(token: String, channelName: String, optionalInfo: String, uid: Int) {
        rtcEngine?.apply {
            this.joinChannel(
                token,
                channelName,
                optionalInfo,
                uid
            )
            this.startPreview()
        }
    }

    override fun leaveChannel(channelName: String) {
        rtcEngine?.apply {
            this.stopPreview()
            this.leaveChannel()
        }
    }

    override fun destroy() {
        Thread {
            RtcEngine.destroy()
            rtcEngine = null
        }
    }
}