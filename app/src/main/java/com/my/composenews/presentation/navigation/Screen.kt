package com.my.composenews.presentation.navigation

sealed interface HomeScreen
sealed interface ChatScreen
sealed interface CallScreen
sealed class Screen(val route: String){

    companion object{
        const val HOME_INITIAL = "home_initial"
        const val CHAT_INITIAL = "chat_initial"
        const val CHAT_CALL = "chat_call"
    }

    object Home: Screen(HOME_INITIAL), HomeScreen

    object Chat: Screen(CHAT_INITIAL), ChatScreen

    object Call: Screen(CHAT_CALL), CallScreen
}
