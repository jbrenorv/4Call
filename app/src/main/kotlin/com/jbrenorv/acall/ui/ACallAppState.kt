package com.jbrenorv.acall.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jbrenorv.acall.core.data.repository.auth.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberACallAppState(
    authRepository: AuthRepository,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): ACallAppState {
    return remember(
        authRepository,
        navController,
        coroutineScope
    ) {
        ACallAppState(
            authRepository = authRepository,
            navController = navController,
            coroutineScope = coroutineScope,
        )
    }
}

@Stable
class ACallAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    authRepository: AuthRepository
) {
    val isLoggedIn: StateFlow<Boolean> = authRepository.userFlow
        .map { authUser -> authUser != null }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = authRepository.hasUser
        )
}
