package com.my.composenews.presentation.navigation.controller

import kotlinx.coroutines.channels.Channel

interface Navigator {

    val instructor: Channel<NavigationIntent>

    fun back(
        route: String? = null,
        inclusive: Boolean = false
    )

    fun to(
        route: String,
        popupToRoute: String? = null,
        inclusive: Boolean = false,
        isSingleTop: Boolean = false
    )
}