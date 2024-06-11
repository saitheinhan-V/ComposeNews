package com.my.composenews.presentation.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.my.composenews.presentation.navigation.Routes
import com.my.composenews.presentation.navigation.Screen
import com.my.composenews.presentation.view.home.HomeScreen

fun NavGraphBuilder.homeGraph(
    navController: NavHostController
){
    navigation(
        startDestination = Screen.Home.route,
        route = Routes.HOME
    ){
        composable(route = Screen.Home.route){
            HomeScreen()
        }
    }
}