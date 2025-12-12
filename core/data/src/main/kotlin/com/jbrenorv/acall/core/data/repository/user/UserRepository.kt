package com.jbrenorv.acall.core.data.repository.user

import com.jbrenorv.acall.core.model.User

interface UserRepository {
    /**
     * Saves the current authenticated user's data
     */
    suspend fun createUser(): Result<User>

    suspend fun getUser(id: String): Result<User>

    suspend fun getUsers(ids: List<String>): Result<List<User>>
}
