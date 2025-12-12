package com.jbrenorv.acall.core.data.model

import com.google.firebase.firestore.DocumentSnapshot
import com.jbrenorv.acall.core.data.exception.DocumentMappingException

fun <T> DocumentSnapshot.requireObject(valueType: Class<T>): T {
    return toObject(valueType)
        ?: throw DocumentMappingException(reference.path, valueType)
}
