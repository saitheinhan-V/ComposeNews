package com.my.composenews.presentation.navigation.controller

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun NavigationInstructor(
    instructor: Channel<NavigationIntent>,
    controller: NavHostController
) {
    val activity = LocalContext.current as? Activity
    LaunchedEffect(
        key1 = activity,
        key2 = controller,
        key3 = instructor
    ) {
        instructor.receiveAsFlow().collect {
            if (activity?.isFinishing == true) {
                return@collect
            }

            when (it) {
                is NavigationIntent.Back -> {
                    if (it.route != null) {
                        controller.popBackStack(it.route, it.inclusive)
                    } else {
                        controller.popBackStack()
                    }
                }

                is NavigationIntent.To -> {
                    controller.navigate(it.route) {
                        launchSingleTop = it.isSingleTop
                        it.popupToRoute?.let { route ->
                            popUpTo(route) {
                                inclusive = it.inclusive
                            }
                        }
                    }
                }
            }
        }
    }
}