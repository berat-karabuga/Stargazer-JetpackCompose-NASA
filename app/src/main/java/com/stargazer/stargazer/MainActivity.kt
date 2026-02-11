package com.stargazer.stargazer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.stargazer.stargazer.presentation.auth.AuthViewModel
import com.stargazer.stargazer.presentation.auth.LoginScreen
import com.stargazer.stargazer.presentation.auth.SignUpScreen
import com.stargazer.stargazer.presentation.home.HomeScreen
import com.stargazer.stargazer.presentation.profile.ProfileScreen
import com.stargazer.stargazer.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val navController = rememberNavController()
                val viewModel: AuthViewModel = hiltViewModel()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val startDestination = if (viewModel.isUserLoggedIn()) Screen.Home else Screen.Login

                val showBottomBar = currentDestination?.route?.contains("Home") == true ||
                        currentDestination?.route?.contains("Profile") == true

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                                    label = { Text("Ana Sayfa") },
                                    selected = currentDestination?.hierarchy?.any { it.route?.contains("Home") == true } == true,
                                    onClick = {
                                        navController.navigate(Screen.Home) {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                                    label = { Text("Profil") },
                                    selected = currentDestination?.hierarchy?.any { it.route?.contains("Profile") == true } == true,
                                    onClick = {
                                        navController.navigate(Screen.Profile) {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Screen.Login> {
                            LoginScreen(
                                onNavigateToSignUp = { navController.navigate(Screen.SignUp) },
                                onNavigateToHome = {
                                    navController.navigate(Screen.Home) {
                                        popUpTo(Screen.Login) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable<Screen.SignUp> {
                            SignUpScreen(onNavigateBack = { navController.popBackStack() })
                        }

                        composable<Screen.Home> {
                            HomeScreen(
                                viewModel = viewModel,
                                onLogout = {
                                    viewModel.logout()
                                    navController.navigate(Screen.Login) {
                                        popUpTo(Screen.Home) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable<Screen.Profile> {
                            ProfileScreen()
                        }
                    }
                }
            }
        }
    }
}