package com.my.composenews.presentation.event

sealed interface MainEvent {

    data class showSnack(val message: String) : MainEvent
}