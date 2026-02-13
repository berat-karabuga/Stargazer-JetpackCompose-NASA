package com.stargazer.stargazer.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.stargazer.stargazer.data.model.UserStats
import com.stargazer.stargazer.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, pass: String): Resource<FirebaseUser>

    suspend fun register(email: String, pass: String, username: String): Resource<FirebaseUser>

    fun logout()

    fun getCurrentUser(): FirebaseUser?

    suspend fun getUserStats(uid: String): Resource<UserStats>
    suspend fun updateUserStreak(uid: String): Resource<UserStats>
}