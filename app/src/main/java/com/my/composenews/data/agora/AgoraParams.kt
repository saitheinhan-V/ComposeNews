package com.my.composenews.data.agora

sealed class AgoraParams {
    data class OnJoinedCurrentUser(val uid: Int) : AgoraParams()
    data class OnJoinedOtherUsers(val uid: Int) : AgoraParams()
    data class OnMuteVideo(val uid: Int, val muted: Boolean) : AgoraParams()
    data class OnMuteAudio(val uid: Int, val muted: Boolean) : AgoraParams()
    data class OnOfflineUser(val uid: Int) : AgoraParams()
    data class Duration(val second: Int) : AgoraParams()
    data class OnActiveUser(val uid: Int) : AgoraParams()
    data object Decline : AgoraParams()
}