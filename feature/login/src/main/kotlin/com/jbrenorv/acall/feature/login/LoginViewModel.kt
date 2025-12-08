package com.jbrenorv.acall.feature.login

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.jbrenorv.acall.core.data.repository.auth.AuthRepository
import com.jbrenorv.acall.feature.login.navigation.LoginRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AuthRepository
) : ViewModel() {
    private val webClientId = savedStateHandle.toRoute<LoginRoute>().webClientId
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loginScreenStarted(context: Context) {
        startLoginWithGoogleFlow(context, true)
    }

    fun loginWithGoogle(context: Context) {
        startLoginWithGoogleFlow(context, false)
    }

    private fun startLoginWithGoogleFlow(
        context: Context,
        useGoogleIdOption: Boolean
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            repository
                .loginWithGoogle(context, useGoogleIdOption, webClientId)
                .onFailure {
                    _isLoading.value = false
                    // TODO: handle failure
                }
        }
    }
}
