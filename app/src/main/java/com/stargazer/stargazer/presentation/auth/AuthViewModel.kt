package com.stargazer.stargazer.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.stargazer.stargazer.domain.repository.AuthRepository
import com.stargazer.stargazer.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {


    private val _loginState = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginState: StateFlow<Resource<FirebaseUser>?> = _loginState.asStateFlow()

    private val _signupState = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signupState: StateFlow<Resource<FirebaseUser>?> = _signupState.asStateFlow()

    fun login(email: String, pass: String) = viewModelScope.launch {
        _loginState.value = Resource.Loading()
        val result = repository.login(email, pass)
        _loginState.value = result
    }

    fun signup(email: String, pass: String) = viewModelScope.launch {
        _signupState.value = Resource.Loading()
        val result = repository.register(email, pass)
        _signupState.value = result
    }

    fun logout() = repository.logout()

    fun isUserLoggedIn(): Boolean {
        return repository.getCurrentUser() != null
    }
}