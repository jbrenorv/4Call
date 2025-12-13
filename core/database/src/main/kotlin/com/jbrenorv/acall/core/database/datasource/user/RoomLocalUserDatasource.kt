package com.jbrenorv.acall.core.database.datasource.user

import com.jbrenorv.acall.core.common.exception.UserNotFoundException
import com.jbrenorv.acall.core.database.dao.UserDao
import com.jbrenorv.acall.core.database.entity.toUser
import com.jbrenorv.acall.core.database.entity.toUserEntity
import com.jbrenorv.acall.core.model.user.User
import javax.inject.Inject

internal class RoomLocalUserDatasource @Inject constructor(
    private val userDao: UserDao
): LocalUserDatasource {
    override fun getUser(userId: String): Result<User> {
        return runCatching {
            val userEntity = userDao.getUserEntity(userId)
                ?: throw UserNotFoundException(userId)

            return Result.success(userEntity.toUser())
        }
    }

    override fun getUsers(ids: List<String>): Result<List<User>> {
        return runCatching {
            val usersEntities = userDao.getUsersEntities(ids.toSet())
            val users = usersEntities.map { it.toUser() }
            return Result.success(users)
        }
    }

    override suspend fun upsertUser(user: User): Result<Unit> {
        return upsertUsers(listOf(user))
    }

    override suspend fun upsertUsers(users: List<User>): Result<Unit> {
        return runCatching {
            val usersEntities = users.map { it.toUserEntity() }
            userDao.upsertUsers(usersEntities)
        }
    }
}
