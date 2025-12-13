package com.jbrenorv.acall.core.network.util

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.jbrenorv.acall.core.network.exception.DocumentMappingException
import kotlinx.datetime.Instant

internal fun <T> DocumentSnapshot.requireObject(valueType: Class<T>): T {
    return toObject(valueType)
        ?: throw DocumentMappingException(reference.path, valueType)
}

internal fun Timestamp.toKotlinInstant() =
    Instant.fromEpochMilliseconds(toInstant().toEpochMilli())
