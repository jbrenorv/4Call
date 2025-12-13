package com.jbrenorv.acall.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jbrenorv.acall.core.model.user.User

@Entity(
    tableName = "users",
)
internal data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    @ColumnInfo(defaultValue = "NULL")
    val photoUrl: String?
)

internal fun UserEntity.toUser() = User(
    id = id,
    name = name,
    photoUrl = photoUrl
)

internal fun User.toUserEntity() = UserEntity(
    id = id,
    name = name,
    photoUrl = photoUrl
)
