package com.jbrenorv.acall.core.data.repository.auth

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jbrenorv.acall.core.common.exception.LoginException
import com.jbrenorv.acall.core.data.model.asUser
import com.jbrenorv.acall.core.model.user.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// TODO: consider move all firebase dependency to network module
internal class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override val userFlow: Flow<User?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            trySend(firebaseUser?.asUser())
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    override fun getCurrentUser(): User? =
        firebaseAuth.currentUser?.asUser()

    override suspend fun loginWithGoogle(
        context: Context,
        useGoogleIdOption: Boolean,
        webClientId: String
    ): Result<User> {
        return runCatching {
            try {
                val currentUser = getCurrentUser()
                if (currentUser != null) {
                    return Result.success(currentUser)
                }

                val credentialOption = getCredentialOption(useGoogleIdOption, webClientId)
                val credential = getCredential(context, credentialOption)
                val user = signInWithCredential(credential)

                return Result.success(user)
            } catch (_: NoCredentialException) {
                throw LoginException("No available credential")
            } catch (_: GetCredentialException) {
                throw LoginException("Unable to load credentials")
            }
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    private fun getCredentialOption(
        useGoogleIdOption: Boolean,
        webClientId: String
    ): CredentialOption {
        return if (useGoogleIdOption) {
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .build()
        } else {
            GetSignInWithGoogleOption
                .Builder(webClientId)
                .build()
        }
    }

    private suspend fun getCredential(
        context: Context,
        credentialOption: CredentialOption
    ): Credential {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(credentialOption)
            .build()

        val result = CredentialManager.create(context).getCredential(
            request = request,
            context = context
        )

        return result.credential
    }

    private suspend fun signInWithCredential(credential: Credential): User {
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                val credential = GoogleAuthProvider.getCredential(idToken, null)

                firebaseAuth.signInWithCredential(credential).await()
                val firebaseUser = firebaseAuth.currentUser!!

                return firebaseUser.asUser()
            } catch (_: Throwable) {
                throw LoginException("Unknown error")
            }
        } else {
            throw LoginException("Invalid credential")
        }
    }
}
