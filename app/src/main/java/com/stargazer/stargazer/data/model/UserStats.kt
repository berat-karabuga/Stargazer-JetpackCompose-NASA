package com.stargazer.stargazer.data.model


data class UserStats(
    val email: String = "",
    val streak: Int = 0,
    val lastLoginDate: String = "",
    val maxStreak: Int = 0
)

