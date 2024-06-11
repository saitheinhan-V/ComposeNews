package com.my.composenews.presentation.navigation.graph

import android.annotation.SuppressLint
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.my.composenews.presentation.navigation.Routes
import com.my.composenews.presentation.navigation.Screen
import com.my.composenews.presentation.view.chat.ChatViewModel
import com.my.composenews.presentation.view.chat.view.CallScreen
import com.my.composenews.presentation.view.chat.view.ChatScreen
import com.my.composenews.sharedVM

@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.chatGraph(
    navController: NavHostController
) {
    navigation(
        route = Routes.CHAT,
        startDestination = Screen.Chat.route
    ) {
        composable(
            route = Screen.Chat.route
        ) { backStackEntry ->
            val vm = backStackEntry.sharedVM<ChatViewModel>(navController = navController)
            ChatScreen(vm = vm)
        }
        composable(
            route = Screen.Call.route
        ) { backStackEntry ->
            val vm = backStackEntry.sharedVM<ChatViewModel>(navController = navController)
            CallScreen(vm = vm)
        }
    }
}