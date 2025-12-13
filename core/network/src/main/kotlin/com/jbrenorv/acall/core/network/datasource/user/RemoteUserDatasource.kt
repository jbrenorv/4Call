package com.jbrenorv.acall.core.network.datasource.user

import com.jbrenorv.acall.core.model.user.User

interface RemoteUserDatasource {
    suspend fun saveUser(user: User): Result<Unit>

    suspend fun getUser(userId: String): Result<User>

    suspend fun getUsers(ids: List<String>): Result<List<User>>
}
