package com.stargazer.stargazer.util

import kotlinx.serialization.Serializable



sealed class Screen {

    @Serializable
    data object Splash : Screen()

    @Serializable
    data object Login : Screen()

    @Serializable
    data object SignUp : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data object Profile : Screen()
}