package com.stargazer.stargazer.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.stargazer.stargazer.data.model.UserStats
import com.stargazer.stargazer.domain.repository.AuthRepository
import com.stargazer.stargazer.util.Resource
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun login(email: String, pass: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, pass).await()

            if (result.user != null) {
                Resource.Success(result.user!!)
            } else {
                Resource.Error("User not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unknown error occurred")
        }
    }


    override suspend fun register(email: String, pass: String, username: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()

            if (result.user != null) {
                val uid = result.user!!.uid
                val newUser = UserStats(
                    email = email,
                    username = username,
                    streak = 1,
                    lastLoginDate = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ISO_DATE),
                    maxStreak = 1
                )
                firestore.collection("users").document(uid).set(newUser).await()

                Resource.Success(result.user!!)
            } else {
                Resource.Error("Registration failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred during registration")
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override suspend fun getUserStats(uid: String): Resource<UserStats> {
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            val stats = document.toObject(UserStats::class.java)
            if (stats != null) {
                Resource.Success(stats)
            } else {
                Resource.Error("User data not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Failed to retrieve data")
        }
    }

    override suspend fun updateUserStreak(uid: String): Resource<UserStats> {
        return try {
            val docRef = firestore.collection("users").document(uid)
            val snapshot = docRef.get().await()

            val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

            var currentStats = snapshot.toObject(UserStats::class.java)

            if (currentStats == null) {
                currentStats = UserStats(
                    email = firebaseAuth.currentUser?.email ?: "",
                    streak = 1,
                    lastLoginDate = today,
                    maxStreak = 1
                )
            } else {
                val lastDate = currentStats.lastLoginDate

                if (lastDate == today) {
                    return Resource.Success(currentStats)
                }

                val yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE)

                if (lastDate == yesterday) {
                    val newStreak = currentStats.streak + 1
                    val newMax = if (newStreak > currentStats.maxStreak) newStreak else currentStats.maxStreak

                    currentStats = currentStats.copy(
                        streak = newStreak,
                        lastLoginDate = today,
                        maxStreak = newMax
                    )
                } else {
                    currentStats = currentStats.copy(
                        streak = 1,
                        lastLoginDate = today
                    )
                }
            }

            docRef.set(currentStats, SetOptions.merge()).await()

            Resource.Success(currentStats)

        } catch (e: Exception) {
            Resource.Error("Streak could not be updated: ${e.localizedMessage}")
        }
    }

}