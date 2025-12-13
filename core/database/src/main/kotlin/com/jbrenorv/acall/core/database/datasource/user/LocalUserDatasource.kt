package com.jbrenorv.acall.core.database.datasource.user

import com.jbrenorv.acall.core.model.user.User

interface LocalUserDatasource {
    fun getUser(userId: String): Result<User>

    fun getUsers(ids: List<String>): Result<List<User>>

    suspend fun upsertUser(user: User): Result<Unit>

    suspend fun upsertUsers(users: List<User>): Result<Unit>
}
