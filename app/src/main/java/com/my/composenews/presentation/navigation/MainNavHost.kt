package com.my.composenews.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.my.composenews.presentation.navigation.graph.chatGraph
import com.my.composenews.presentation.navigation.graph.homeGraph

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
   NavHost(
       navController = navController,
       startDestination = Routes.HOME,
       route = Routes.ROOT,
       modifier = modifier
   )
   {
       homeGraph(navController)
       chatGraph(navController)
   }
}