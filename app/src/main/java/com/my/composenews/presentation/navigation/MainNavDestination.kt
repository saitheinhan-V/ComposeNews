package com.my.composenews.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.my.composenews.R

interface Destination {
    val icon: ImageVector
    val route: String
    val resourceId: Int
}

object Home : Destination {
    override val icon = Icons.Filled.Home
    override val route = "home_initial"
    override val resourceId = R.string.home
}

object Chat : Destination {
    override val icon = Icons.Filled.Chat
    override val route = "chat_initial"
    override val resourceId = R.string.chat
}

val TabRowScreens = listOf(Home, Chat)
val TabRoutes = listOf(Home.route, Chat.route)
