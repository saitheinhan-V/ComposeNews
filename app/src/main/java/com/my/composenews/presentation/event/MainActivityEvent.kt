package com.my.composenews.presentation.event

import com.my.composenews.domain.vo.MessageVo

sealed interface MainActivityEvent {

    data class MessageEvent(val msg: MessageVo): MainActivityEvent
}