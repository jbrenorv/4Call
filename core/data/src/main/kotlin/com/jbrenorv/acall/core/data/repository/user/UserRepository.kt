package com.jbrenorv.acall.core.data.repository.user

import com.jbrenorv.acall.core.model.user.User

interface UserRepository {
    /**
     * Saves the current authenticated user's data
     */
    suspend fun saveUser(): Result<User>

    suspend fun getUser(userId: String, forceRefresh: Boolean = false): Result<User>

    suspend fun getUsers(ids: List<String>, forceRefresh: Boolean = false): Result<List<User>>
}
