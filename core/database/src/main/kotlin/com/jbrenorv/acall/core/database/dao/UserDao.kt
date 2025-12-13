package com.jbrenorv.acall.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jbrenorv.acall.core.database.entity.UserEntity

@Dao
internal interface UserDao {
    @Query(
        value = """
        SELECT * FROM users
        WHERE id = :userId
    """,
    )
    fun getUserEntity(userId: String): UserEntity?

    @Query(
        value = """
        SELECT * FROM users
        WHERE id IN (:ids)
    """,
    )
    fun getUsersEntities(ids: Set<String>): List<UserEntity>

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    @Upsert
    suspend fun upsertUsers(entities: List<UserEntity>)
}
