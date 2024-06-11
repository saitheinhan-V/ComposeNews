package com.my.composenews.presentation.navigation.controller

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class NavigatorImpl @Inject constructor() : Navigator {
    override val instructor: Channel<NavigationIntent> = Channel(
        capacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    override fun back(route: String?, inclusive: Boolean) {
        instructor.trySend(
            NavigationIntent.Back(
                route = route,
                inclusive = inclusive
            )
        )
    }

    override fun to(
        route: String,
        popupToRoute: String?,
        inclusive: Boolean,
        isSingleTop: Boolean
    ) {
        instructor.trySend(
            NavigationIntent.To(
                route = route,
                popupToRoute = popupToRoute,
                inclusive = inclusive,
                isSingleTop = isSingleTop
            )
        )
    }
}
