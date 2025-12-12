package com.jbrenorv.acall.core.model

data class User(
    val id: String,
    val name: String,
    val photoUrl: String?
)

fun AuthUser.toUser() = User(
    id = id,
    name = name,
    photoUrl = photoUrl
)
