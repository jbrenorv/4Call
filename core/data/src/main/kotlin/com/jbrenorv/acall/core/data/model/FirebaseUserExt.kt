package com.jbrenorv.acall.core.data.model

import com.google.firebase.auth.FirebaseUser
import com.jbrenorv.acall.core.model.AuthUser

fun FirebaseUser.asAuthUser(): AuthUser = AuthUser(
    uid = uid,
    name = displayName ?: "",
    photoUrl = photoUrl?.toString()
)
