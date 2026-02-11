package com.stargazer.stargazer.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.stargazer.stargazer.data.model.UserStats
import com.stargazer.stargazer.domain.repository.AuthRepository
import com.stargazer.stargazer.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _userStats = MutableStateFlow<Resource<UserStats>>(Resource.Loading())
    val userStats: StateFlow<Resource<UserStats>> = _userStats.asStateFlow()

    init {
        checkAndUpdateStreak()
    }

    private fun checkAndUpdateStreak() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                _userStats.value = Resource.Loading()
                val result = repository.updateUserStreak(uid)
                _userStats.value = result
            }
        }
    }
}