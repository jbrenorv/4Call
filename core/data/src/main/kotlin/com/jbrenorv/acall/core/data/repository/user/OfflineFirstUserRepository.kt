package com.jbrenorv.acall.core.data.repository.user

import com.jbrenorv.acall.core.common.exception.NoAuthUserException
import com.jbrenorv.acall.core.data.repository.auth.AuthRepository
import com.jbrenorv.acall.core.database.datasource.user.LocalUserDatasource
import com.jbrenorv.acall.core.model.user.User
import com.jbrenorv.acall.core.network.datasource.user.RemoteUserDatasource
import javax.inject.Inject

internal class OfflineFirstUserRepository @Inject constructor(
    private val authRepository: AuthRepository,
    private val localUserDatasource: LocalUserDatasource,
    private val remoteUserDatasource: RemoteUserDatasource
) : UserRepository {
    override suspend fun saveUser(): Result<User> {
        return runCatching {
            val user = authRepository.getCurrentUser()
                ?: throw NoAuthUserException()
            val result = remoteUserDatasource.saveUser(user)
            result.exceptionOrNull()?.let { exception ->
                return Result.failure(exception)
            }
            localUserDatasource.upsertUser(user)
            return Result.success(user)
        }
    }

    override suspend fun getUser(userId: String, forceRefresh: Boolean): Result<User> {
        if (forceRefresh) {
            return fetchUser(userId)
        }
        val result = localUserDatasource.getUser(userId)
        if (result.isFailure) {
            return fetchUser(userId)
        }
        return result
    }

    override suspend fun getUsers(ids: List<String>, forceRefresh: Boolean): Result<List<User>> {
        if (forceRefresh) {
            return fetchUsers(ids)
        }

        val localUsersResult = localUserDatasource.getUsers(ids)
        val localUsers = localUsersResult.getOrNull() ?: return fetchUsers(ids)

        val foundIds = localUsers.map { it.id }
        val missingIds = ids - foundIds
        val remoteUsersResult = if (missingIds.isNotEmpty()) {
            fetchUsers(missingIds)
        } else {
            Result.success(emptyList())
        }
        val remoteUsers = remoteUsersResult.getOrElse { exception ->
            return Result.failure(exception)
        }

        val users = localUsers + remoteUsers
        return Result.success(users)
    }

    private suspend fun fetchUser(userId: String): Result<User> {
        val result = remoteUserDatasource.getUser(userId)
        result.getOrNull()?.let { user ->
            localUserDatasource.upsertUser(user)
        }
        return result
    }

    private suspend fun fetchUsers(ids: List<String>): Result<List<User>> {
        val result = remoteUserDatasource.getUsers(ids)
        result.getOrNull()?.let { users ->
            localUserDatasource.upsertUsers(users)
        }
        return result
    }
}
