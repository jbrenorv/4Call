package com.jbrenorv.acall.core.data.repository.auth

import android.content.Context
import com.jbrenorv.acall.core.model.AuthUser
import kotlinx.coroutines.flow.Flow

class LoginException(override val message: String) : Exception()

interface AuthRepository {
    val authUser: Flow<AuthUser?>

    val hasUser: Boolean
        get() = getCurrentAuthUser() != null

    fun getCurrentAuthUser(): AuthUser?

    suspend fun loginWithGoogle(
        context: Context,
        useGoogleIdOption: Boolean,
        webClientId: String
    ): Result<AuthUser>

    suspend fun logout()
}
