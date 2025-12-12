package com.jbrenorv.acall.core.data.exception

open class MappingException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
