package com.stargazer.stargazer.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stargazer.stargazer.data.model.ApodModel
import com.stargazer.stargazer.domain.repository.NasaRepository
import com.stargazer.stargazer.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NasaRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow<Resource<ApodModel>>(Resource.Loading())
    val homeState: StateFlow<Resource<ApodModel>> = _homeState.asStateFlow()

    init {
        getTodaysPhoto()
    }

    fun getTodaysPhoto() {
        viewModelScope.launch {
            _homeState.value = Resource.Loading()
            val result = repository.getDailyPhoto()
            _homeState.value = result
        }
    }
}