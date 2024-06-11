package com.my.composenews.presentation.navigation.controller

sealed interface NavigationIntent {

    data class Back(
        val route: String? = null,
        val inclusive: Boolean = false
    ) : NavigationIntent

    data class To(
        val route: String,
        val popupToRoute: String? = null,
        val inclusive: Boolean = false,
        val isSingleTop: Boolean = false
    ) : NavigationIntent
}