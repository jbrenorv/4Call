package com.jbrenorv.acall.core.data.exception

class DocumentMappingException(
    path: String,
    target: Class<*>,
    cause: Throwable? = null
) : MappingException(
    "Failed to map document $path to ${target.simpleName}",
    cause
)
