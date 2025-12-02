package com.stargazer.stargazer

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

sealed class Screen(val route: String){
    object Entry : Screen("entry_screen")
    object Second : Screen("second_screen")
}

@Composable
fun navi(navController: NavHostController, startDestination: String){
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(route = Screen.Entry.route){
            entryScreen(navController)
        }

        composable(route = Screen.Second.route){
            second(navController)
        }
    }
}