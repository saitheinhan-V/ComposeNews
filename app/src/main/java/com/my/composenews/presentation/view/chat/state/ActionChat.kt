package com.my.composenews.presentation.view.chat.state

sealed interface ActionChat {

    data class ChangeUser(val id: String): ActionChat

    data class ClickJoin(val uid: Int): ActionChat

    data object ClearUser: ActionChat
}