package com.jbrenorv.acall.core.network.document

import com.google.firebase.firestore.DocumentSnapshot
import com.jbrenorv.acall.core.model.user.User
import com.jbrenorv.acall.core.network.util.requireObject

internal data class UserDocument(
    val id: String = "",
    val name: String = "",
    val photoUrl: String? = null
) {
    companion object
}

internal fun UserDocument.Companion.create(user: User) = UserDocument(
    id = user.id,
    name = user.name,
    photoUrl = user.photoUrl
)

internal fun UserDocument.toUser() = User(
    id = id,
    name = name,
    photoUrl = photoUrl
)

internal fun DocumentSnapshot.toUserDocument() = requireObject(UserDocument::class.java)
