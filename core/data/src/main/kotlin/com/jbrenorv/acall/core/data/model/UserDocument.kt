package com.jbrenorv.acall.core.data.model

import com.google.firebase.firestore.DocumentSnapshot
import com.jbrenorv.acall.core.model.AuthUser
import com.jbrenorv.acall.core.model.User

internal data class UserDocument(
    val id: String = "",
    val name: String = "",
    val photoUrl: String? = null
) {
    companion object
}

internal fun UserDocument.Companion.create(authUser: AuthUser) = UserDocument(
    id = authUser.id,
    name = authUser.name,
    photoUrl = authUser.photoUrl
)

internal fun UserDocument.toUser() = User(
    id = id,
    name = name,
    photoUrl = photoUrl
)

internal fun DocumentSnapshot.toUserDocument() = requireObject(UserDocument::class.java)
