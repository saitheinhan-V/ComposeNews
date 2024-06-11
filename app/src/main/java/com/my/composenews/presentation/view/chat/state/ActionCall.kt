package com.my.composenews.presentation.view.chat.state

sealed interface ActionCall {

    data object ClickCallEnd : ActionCall

    data class ClickCamera(val enabled: Boolean): ActionCall

    data class ClickMute(val enabled: Boolean): ActionCall

    data class ClickFlip(val enabled: Boolean): ActionCall
}