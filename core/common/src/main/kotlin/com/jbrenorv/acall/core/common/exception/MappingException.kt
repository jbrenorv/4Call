package com.jbrenorv.acall.core.common.exception

open class MappingException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
