package com.jbrenorv.acall.core.network.exception

import com.jbrenorv.acall.core.common.exception.MappingException

class DocumentMappingException(
    path: String,
    target: Class<*>,
    cause: Throwable? = null
) : MappingException(
    "Failed to map document $path to ${target.simpleName}",
    cause
)
