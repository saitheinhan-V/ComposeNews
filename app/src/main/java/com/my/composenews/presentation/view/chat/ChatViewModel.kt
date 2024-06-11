package com.my.composenews.presentation.view.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.composenews.data.AppConstant
import com.my.composenews.data.agora.AgoraHandler
import com.my.composenews.data.agora.AgoraParams
import com.my.composenews.data.agora.TokenUtils
import com.my.composenews.domain.form.ChannelError
import com.my.composenews.presentation.navigation.Screen
import com.my.composenews.presentation.navigation.controller.Navigator
import com.my.composenews.presentation.view.chat.state.ActionCall
import com.my.composenews.presentation.view.chat.state.ActionChat
import com.my.composenews.presentation.view.chat.state.ChatViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val agoraHandler: AgoraHandler,
    private val appNavigator: Navigator
) : ViewModel() {

    private val vmState = MutableStateFlow(ChatViewModelState())

    val currentChannel = vmState
        .map(ChatViewModelState::asCurrentChannelId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.asCurrentChannelId()
        )
    val channelError = vmState
        .map(ChatViewModelState::asChannelError)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.asChannelError()
        )
    val localView = vmState
        .map(ChatViewModelState::asLocalView)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.asLocalView()
        )
    val remoteView = vmState
        .map(ChatViewModelState::asRemoteView)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.asRemoteView()
        )
    val showRemote = vmState
        .map(ChatViewModelState::showRemote)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.showRemote()
        )
    val showLocal = vmState
        .map(ChatViewModelState::showLocal)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.showLocal()
        )
    val isFlip = vmState
        .map(ChatViewModelState::isActiveFlip)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.isActiveFlip()
        )
    val isSpeaker = vmState
        .map(ChatViewModelState::isActiveSpeaker)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.isActiveSpeaker()
        )

    val isCamera = vmState
        .map(ChatViewModelState::isActiveCamera)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.isActiveCamera()
        )

    val isMute = vmState
        .map(ChatViewModelState::isActiveMute)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = vmState.value.isActiveMute()
        )

    init {
        initEngine()
        listenEvent()
    }

    private fun initEngine() {
        viewModelScope.launch {
            agoraHandler.setupProfile()
            agoraHandler.setupVideo()
        }
    }

    private fun listenEvent() {
        viewModelScope.launch {
            agoraHandler.agoraParams.collectLatest {
                when (it) {
                    AgoraParams.Decline -> {

                    }

                    is AgoraParams.Duration -> {

                    }

                    is AgoraParams.OnActiveUser -> {

                    }

                    is AgoraParams.OnJoinedCurrentUser -> {
                        val localView = agoraHandler.setupLocalView(it.uid)
                        vmState.update { state ->
                            state.copy(
                                localView = localView,
                                camera = false,
                                shouldShowLocal = false
                            )
                        }
                    }

                    is AgoraParams.OnJoinedOtherUsers -> {
                        val view = agoraHandler.setupRemoteView(it.uid)
                        vmState.update { state ->
                            state.copy(
                                remoteView = view,
                                shouldShowRemote = true
                            )
                        }
                    }

                    is AgoraParams.OnMuteAudio -> {
                        if (it.uid == vmState.value.localView!!.first) {
                            agoraHandler.muteLocalAudio(it.muted)
                        }
                    }

                    is AgoraParams.OnMuteVideo -> {
                        if (it.uid == vmState.value.localView!!.first) {
                            agoraHandler.muteLocalVideo(it.muted)
                        } else {
                            agoraHandler.muteRemoteVideo(id = it.uid, muted = it.muted)
                            vmState.update { state ->
                                state.copy(
                                    shouldShowRemote = !it.muted
                                )
                            }
                        }
                    }

                    is AgoraParams.OnOfflineUser -> {//remote user leave
                        vmState.update { state ->
                            state.copy(
                                shouldShowRemote = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun onAction(action: ActionChat) {
        when (action) {
            is ActionChat.ChangeUser -> {
                viewModelScope.launch {
                    vmState.update { state ->
                        state.copy(
                            currentChannelId = action.id,
                            channelError = ChannelError(
                                isError = action.id.isEmpty(),
                                errMsg = "Channel id required!"
                            )
                        )
                    }
                }
            }

            is ActionChat.ClickJoin -> {
                viewModelScope.launch {

                    vmState.update { state ->
                        state.copy(
                            channelError = ChannelError(
                                isError = vmState.value.currentChannelId.isEmpty(),
                                errMsg = "Channel id required!"
                            )
                        )
                    }

                    if(vmState.value.channelError.isError) return@launch

                    val token = AppConstant.AGORA_TOKEN

                    val localView = agoraHandler.setupLocalView(action.uid)
                    vmState.update { state ->
                        state.copy(
                            localView = localView,
                            camera = false,
                            shouldShowLocal = false
                        )
                    }

                    agoraHandler.joinChannel(
                        token = token,
                        channelName = vmState.value.currentChannelId,
                        "",
                        action.uid
                    )

                    appNavigator.to(
                        route = Screen.Call.route
                    )
                }
            }

            ActionChat.ClearUser -> {
                viewModelScope.launch {
                    vmState.update { state ->
                        state.copy(
                            currentChannelId = ""
                        )
                    }
                }
            }
        }
    }

    //generate token
    private fun generateToken(uid: String) {
        TokenUtils.generate(
            AppConstant.AGORA_APP_ID,
            AppConstant.CERTIFICATE,
            channelName = vmState.value.currentChannelId,
            uid = uid.toInt(),
            onGetToken = {
                it?.let {
                    vmState.update { state ->
                        state.copy(
                            token = it
                        )
                    }
                }
            }
        )
    }

    fun onActionCall(action: ActionCall) {
        when (action) {
            ActionCall.ClickCallEnd -> {
                agoraHandler.leaveChannel(vmState.value.currentChannelId)
                agoraHandler.destroy()
                appNavigator.back()
                vmState.update { state ->
                    state.copy(
                        camera = false,
                        mute = true,
                        shouldShowLocal = false,
                        shouldShowRemote = false,
                        flip = false,
                        currentChannelId = ""
                    )
                }
            }

            is ActionCall.ClickCamera -> {
                agoraHandler.muteLocalVideo(action.enabled)
                vmState.update { state ->
                    state.copy(
                        camera = !action.enabled,
                        shouldShowLocal = !action.enabled
                    )
                }
            }

            is ActionCall.ClickFlip -> {
                agoraHandler.flipCamera(action.enabled)
                vmState.update { state ->
                    state.copy(
                        flip = action.enabled
                    )
                }
            }

            is ActionCall.ClickMute -> {
                agoraHandler.muteLocalAudio(action.enabled)
                vmState.update { state ->
                    state.copy(
                        mute = action.enabled
                    )
                }
            }
        }
    }
}