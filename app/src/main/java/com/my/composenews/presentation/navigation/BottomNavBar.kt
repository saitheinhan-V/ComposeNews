package com.my.composenews.presentation.navigation

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.my.composenews.R

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val showBottomNav = navController.currentDestination?.route in TabRoutes

    if (showBottomNav) {
        NavigationBar {
            TabRowScreens.forEach {
                NavigationBarItem(
                    selected = currentDestination?.route == it.route,
                    onClick = {
                        Log.i("current.time",System.currentTimeMillis().toString())
                        navController.navigate(it.route) {
//                            navController.graph.startDestinationRoute?.let {route ->
//                                popUpTo(route){
//                                    saveState = true
//                                }
//                            }
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = "BottomBar Icon",
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = it.resourceId)
                        )
                    }
                )
            }
        }
    }
}