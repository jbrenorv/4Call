package com.jbrenorv.acall.core.data.repository.auth

import android.content.Context
import com.jbrenorv.acall.core.model.user.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val userFlow: Flow<User?>

    val hasUser: Boolean
        get() = getCurrentUser() != null

    fun getCurrentUser(): User?

    suspend fun loginWithGoogle(
        context: Context,
        useGoogleIdOption: Boolean,
        webClientId: String
    ): Result<User>

    suspend fun logout()
}
