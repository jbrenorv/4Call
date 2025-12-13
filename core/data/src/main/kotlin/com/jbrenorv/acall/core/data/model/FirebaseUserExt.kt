package com.jbrenorv.acall.core.data.model

import com.google.firebase.auth.FirebaseUser
import com.jbrenorv.acall.core.model.user.User

fun FirebaseUser.asUser(): User = User(
    id = uid,
    name = displayName ?: "",
    photoUrl = photoUrl?.toString()
)
